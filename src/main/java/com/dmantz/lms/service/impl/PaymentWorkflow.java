package com.dmantz.lms.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.config.SecurityUtils;
import com.dmantz.lms.dto.response.CheckoutResponse;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.CourseFee;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentSource;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.EnrollmentType;
import com.dmantz.lms.entity.Payment;
import com.dmantz.lms.entity.PaymentMethod;
import com.dmantz.lms.entity.PaymentStatus;
import com.dmantz.lms.entity.Program;
import com.dmantz.lms.entity.ProgramFee;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.DuplicateEnrollmentException;
import com.dmantz.lms.exceptions.PaymentGatewayException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.repository.CourseFeeRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.PaymentRepository;
import com.dmantz.lms.repository.ProgramFeeRepository;
import com.dmantz.lms.repository.ProgramRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

/*
 * Shared payment workflow used by student checkout, admin enrollment, webhooks and the
 * expiry scheduler.
 *
 * Core rule: an enrollment row is created ONLY by settle(), and settle() is called only for
 * a payment that is PAID (online, offline, waiver or free).
 */
@Component
@Transactional
public class PaymentWorkflow {

    private static final Logger logger = LogManager.getLogger(PaymentWorkflow.class);

    // Online requests that can still be paid
    static final List<PaymentStatus> OPEN_STATUSES = List.of(PaymentStatus.CREATED, PaymentStatus.FAILED);

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentBatchRepository enrollmentBatchRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;
    private final CourseFeeRepository courseFeeRepository;
    private final ProgramFeeRepository programFeeRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.currency:INR}")
    private String currency;

    public PaymentWorkflow(
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            EnrollmentBatchRepository enrollmentBatchRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            ProgramRepository programRepository,
            CourseFeeRepository courseFeeRepository,
            ProgramFeeRepository programFeeRepository,
            RazorpayClient razorpayClient) {

        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentBatchRepository = enrollmentBatchRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.programRepository = programRepository;
        this.courseFeeRepository = courseFeeRepository;
        this.programFeeRepository = programFeeRepository;
        this.razorpayClient = razorpayClient;
    }

    // ============================================================
    // TYPES
    // ============================================================

    /*
     * What is being bought: a course or a program
     */
    public record PurchaseTarget(EnrollmentType type, Course course, Program program) {

        public String id() {
            return type == EnrollmentType.COURSE ? course.getCourseId() : program.getProgramId();
        }

        public String name() {
            return type == EnrollmentType.COURSE ? course.getCourseTitle() : program.getProgramTitle();
        }
    }

    public record FeeQuote(BigDecimal fee, BigDecimal discount, BigDecimal payable) {

        public long payableInPaise() {
            return payable.multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();
        }

        public boolean isFree() {
            return payable.signum() == 0;
        }
    }

    // ============================================================
    // LOOKUPS
    // ============================================================

    public PurchaseTarget resolveTarget(String courseId, String programId) {

        boolean hasCourse = courseId != null && !courseId.isBlank();
        boolean hasProgram = programId != null && !programId.isBlank();

        if (hasCourse == hasProgram) {
            throw new BadRequestException("Send exactly one of courseId or programId");
        }

        if (hasCourse) {
            Course course = courseRepository.findByCourseIdAndIsDeletedFalse(courseId.trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
            return new PurchaseTarget(EnrollmentType.COURSE, course, null);
        }

        Program program = programRepository.findByProgramId(programId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Program not found: " + programId));
        return new PurchaseTarget(EnrollmentType.PROGRAM, null, program);
    }

    public PurchaseTarget targetOf(Payment payment) {
        return new PurchaseTarget(payment.getEnrollmentType(), payment.getCourse(), payment.getProgram());
    }

    /*
     * Fee currently in effect. Always calculated on the server.
     */
    public FeeQuote quote(PurchaseTarget target) {

        LocalDate today = LocalDate.now();
        BigDecimal fee;
        BigDecimal discount;

        if (target.type() == EnrollmentType.COURSE) {
            CourseFee courseFee = courseFeeRepository
                    .findTopByCourse_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                            target.course().getId(), today)
                    .orElseThrow(() -> new ResourceNotFoundException("No fee configured for this course"));
            fee = courseFee.getFee();
            discount = courseFee.getDiscount();
        } else {
            ProgramFee programFee = programFeeRepository
                    .findTopByProgram_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                            target.program().getId(), today)
                    .orElseThrow(() -> new ResourceNotFoundException("No fee configured for this program"));
            fee = programFee.getFee();
            discount = programFee.getDiscount();
        }

        fee = fee != null ? fee : BigDecimal.ZERO;
        discount = discount != null ? discount : BigDecimal.ZERO;
        BigDecimal payable = fee.subtract(discount).max(BigDecimal.ZERO);

        return new FeeQuote(
                fee.setScale(2, RoundingMode.HALF_UP),
                discount.setScale(2, RoundingMode.HALF_UP),
                payable.setScale(2, RoundingMode.HALF_UP));
    }

    /*
     * Locks the student row: every path that may create an enrollment goes through this lock,
     * so two concurrent payments cannot create two enrollments.
     */
    public Student lockStudent(String studentId) {
        return studentRepository.findByStudentIdForUpdate(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
    }

    public Optional<Enrollment> findCurrentEnrollment(Student student, PurchaseTarget target) {

        if (target.type() == EnrollmentType.COURSE) {
            return enrollmentRepository.findFirstByStudentStudentIdAndCourseCourseIdAndStatusNot(
                    student.getStudentId(), target.id(), EnrollmentStatus.CANCELLED);
        }
        return enrollmentRepository.findFirstByStudentStudentIdAndProgramProgramIdAndStatusNot(
                student.getStudentId(), target.id(), EnrollmentStatus.CANCELLED);
    }

    public void assertNotEnrolled(Student student, PurchaseTarget target) {

        findCurrentEnrollment(student, target).ifPresent(e -> {
            throw new DuplicateEnrollmentException(
                    "Student " + student.getStudentId() + " is already enrolled in " + target.name()
                            + " (enrollment #" + e.getId() + ")");
        });
    }

    /*
     * Latest online request for this student and target that can still be paid
     */
    public Optional<Payment> findOpenRequest(Student student, PurchaseTarget target) {

        List<Payment> candidates = target.type() == EnrollmentType.COURSE
                ? paymentRepository.findByStudent_StudentIdAndCourse_CourseIdAndStatusInOrderByIdDesc(
                        student.getStudentId(), target.id(), OPEN_STATUSES)
                : paymentRepository.findByStudent_StudentIdAndProgram_ProgramIdAndStatusInOrderByIdDesc(
                        student.getStudentId(), target.id(), OPEN_STATUSES);

        LocalDateTime now = LocalDateTime.now();
        return candidates.stream()
                .filter(p -> p.getMethod() == PaymentMethod.ONLINE)
                .filter(p -> p.getExpiresAt() == null || p.getExpiresAt().isAfter(now))
                .findFirst();
    }

    // ============================================================
    // CREATE PAYMENTS
    // ============================================================

    public Payment newPayment(Student student, PurchaseTarget target, FeeQuote quote,
                              PaymentMethod method, PaymentStatus status) {

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setEnrollmentType(target.type());
        payment.setCourse(target.course());
        payment.setProgram(target.program());
        payment.setMethod(method);
        payment.setStatus(status);
        payment.setFeeAmount(quote.fee());
        payment.setDiscountAmount(quote.discount());
        payment.setAmount(quote.payable());
        payment.setCurrency(currency);
        payment.setInitiatedBy(SecurityUtils.getCurrentUser());
        return payment;
    }

    /*
     * Creates a Razorpay order and stores it as a CREATED payment. No enrollment yet.
     */
    public Payment createOnlineRequest(Student student, PurchaseTarget target, FeeQuote quote, int expiryHours) {

        // Razorpay minimum order amount is 100 paise
        if (quote.payableInPaise() < 100) {
            throw new BadRequestException("Payable amount must be at least 1.00 " + currency);
        }

        Payment payment = newPayment(student, target, quote, PaymentMethod.ONLINE, PaymentStatus.CREATED);
        payment.setExpiresAt(LocalDateTime.now().plusHours(expiryHours));

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", quote.payableInPaise());
            orderRequest.put("currency", currency);
            // receipt is limited to 40 characters by Razorpay
            orderRequest.put("receipt", "lms_" + student.getStudentId() + "_" + System.currentTimeMillis());

            JSONObject notes = new JSONObject();
            notes.put("studentId", student.getStudentId());
            notes.put("enrollmentType", target.type().name());
            notes.put("targetId", target.id());
            notes.put("initiatedBy", String.valueOf(payment.getInitiatedBy()));
            orderRequest.put("notes", notes);

            Order order = razorpayClient.orders.create(orderRequest);
            payment.setRazorpayOrderId(order.get("id"));

        } catch (RazorpayException e) {
            logger.error("Razorpay order creation failed for student {} target {}",
                    student.getStudentId(), target.id(), e);
            throw new PaymentGatewayException("Unable to create payment order. Please try again.", e);
        }

        paymentRepository.save(payment);

        logger.info("Razorpay order {} created: payment #{} student {} {} {} amount {}",
                payment.getRazorpayOrderId(), payment.getId(), student.getStudentId(),
                target.type(), target.id(), quote.payable());

        return payment;
    }

    /*
     * Fee is 0: record a FREE payment and enroll immediately
     */
    public Payment settleFree(Student student, PurchaseTarget target, FeeQuote quote) {

        Payment payment = newPayment(student, target, quote, PaymentMethod.FREE, PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);
        settle(payment);
        return payment;
    }

    // ============================================================
    // SETTLEMENT (the only place enrollments are created)
    // ============================================================

    /*
     * Marks an online payment PAID (from verify, webhook or the expiry scheduler) and enrolls.
     * Idempotent: a payment that is already PAID / refunded is left alone.
     * The caller must hold the payment row lock.
     */
    public void markOnlinePaid(Payment payment, String razorpayPaymentId, String signature) {

        if (payment.getStatus() == PaymentStatus.PAID
                || payment.getStatus() == PaymentStatus.REFUND_PENDING
                || payment.getStatus() == PaymentStatus.REFUNDED) {
            logger.info("Payment #{} (order {}) is already {}, ignoring",
                    payment.getId(), payment.getRazorpayOrderId(), payment.getStatus());
            return;
        }

        // A CANCELLED / EXPIRED order can still be captured by Razorpay: money was taken,
        // so it is settled (and refunded by settle() if the student is already enrolled).
        payment.setStatus(PaymentStatus.PAID);
        if (razorpayPaymentId != null) {
            payment.setRazorpayPaymentId(razorpayPaymentId);
        }
        if (signature != null) {
            payment.setRazorpaySignature(signature);
        }
        payment.setFailureReason(null);
        payment.setPaidAt(LocalDateTime.now());

        settle(payment);
    }

    public void markOnlineFailed(Payment payment, String razorpayPaymentId, String reason) {

        if (payment.getStatus() != PaymentStatus.CREATED && payment.getStatus() != PaymentStatus.FAILED) {
            // A failed attempt must never override a settled / cancelled order
            logger.info("Ignoring payment.failed for payment #{} in status {}", payment.getId(), payment.getStatus());
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);
        if (razorpayPaymentId != null) {
            payment.setRazorpayPaymentId(razorpayPaymentId);
        }
        payment.setFailureReason(truncate(reason, 500));

        logger.info("Payment #{} (order {}) FAILED: {}", payment.getId(), payment.getRazorpayOrderId(), reason);
    }

    /*
     * Creates the enrollment for a PAID payment.
     * If the student is already enrolled (duplicate payment), the payment is refunded instead.
     */
    public Enrollment settle(Payment payment) {

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException("Only PAID payments can be settled, payment #" + payment.getId());
        }

        if (payment.getEnrollment() != null) {
            return payment.getEnrollment();
        }

        Student student = lockStudent(payment.getStudent().getStudentId());
        PurchaseTarget target = targetOf(payment);

        Optional<Enrollment> existing = findCurrentEnrollment(student, target);
        if (existing.isPresent()) {
            handleDuplicateSettlement(payment, existing.get());
            return null;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setEnrollmentType(target.type());
        enrollment.setCourse(target.course());
        enrollment.setProgram(target.program());
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setPaymentStatus(paymentStatusLabel(payment.getMethod()));
        enrollment.setSource(isSelfService(payment, student) ? EnrollmentSource.SELF : EnrollmentSource.ADMIN);
        enrollmentRepository.save(enrollment);

        payment.setEnrollment(enrollment);

        cancelOtherOpenRequests(student, target, payment.getId());

        logger.info("Enrollment #{} created: student {} {} {} via payment #{} ({})",
                enrollment.getId(), student.getStudentId(), target.type(), target.id(),
                payment.getId(), payment.getMethod());

        return enrollment;
    }

    private void handleDuplicateSettlement(Payment payment, Enrollment existing) {

        String reason = "Duplicate payment: student already enrolled (enrollment #" + existing.getId() + ")";
        logger.warn("Payment #{} settled for student {} who is already enrolled (#{}), refunding",
                payment.getId(), payment.getStudent().getStudentId(), existing.getId());

        if (payment.getMethod() == PaymentMethod.ONLINE && payment.getAmount().signum() > 0) {
            try {
                refundOnline(payment, payment.getAmount(), reason);
            } catch (PaymentGatewayException e) {
                // Money is kept as PAID without an enrollment; admin must refund manually
                payment.setRemarks(truncate("AUTO REFUND FAILED, refund manually. " + reason, 255));
                logger.error("Automatic refund failed for duplicate payment #{}", payment.getId(), e);
            }
        } else {
            payment.setStatus(PaymentStatus.CANCELLED);
            payment.setRemarks(reason);
        }
    }

    /*
     * Once enrolled, any other unpaid online orders for the same course / program are void.
     * If one of them is paid anyway, settle() refunds it as a duplicate.
     */
    private void cancelOtherOpenRequests(Student student, PurchaseTarget target, Long settledPaymentId) {

        List<Payment> open = target.type() == EnrollmentType.COURSE
                ? paymentRepository.findByStudent_StudentIdAndCourse_CourseIdAndStatusInOrderByIdDesc(
                        student.getStudentId(), target.id(), OPEN_STATUSES)
                : paymentRepository.findByStudent_StudentIdAndProgram_ProgramIdAndStatusInOrderByIdDesc(
                        student.getStudentId(), target.id(), OPEN_STATUSES);

        for (Payment p : open) {
            if (!p.getId().equals(settledPaymentId)) {
                p.setStatus(PaymentStatus.CANCELLED);
                p.setRemarks("Replaced by payment #" + settledPaymentId);
            }
        }
    }

    /*
     * Called by the expiry scheduler for an online order past its expiry time.
     * If Razorpay has captured a payment for it (missed webhook), it is settled; otherwise EXPIRED.
     */
    public void expireOrRecover(Long paymentId) {

        Payment payment = paymentRepository.findByIdForUpdate(paymentId).orElse(null);
        if (payment == null || !OPEN_STATUSES.contains(payment.getStatus())) {
            return;
        }

        List<com.razorpay.Payment> attempts;
        try {
            attempts = razorpayClient.orders.fetchPayments(payment.getRazorpayOrderId());
        } catch (RazorpayException e) {
            throw new PaymentGatewayException("Unable to check order " + payment.getRazorpayOrderId(), e);
        }

        for (com.razorpay.Payment attempt : attempts) {
            String status = attempt.get("status");
            if ("captured".equals(status)) {
                logger.warn("Order {} was captured but not settled (missed webhook), settling now",
                        payment.getRazorpayOrderId());
                markOnlinePaid(payment, attempt.get("id"), null);
                return;
            }
            if ("authorized".equals(status)) {
                // Capture is still in progress at Razorpay; check again on the next run
                logger.info("Order {} has an authorized payment, not expiring yet", payment.getRazorpayOrderId());
                return;
            }
        }

        payment.setStatus(PaymentStatus.EXPIRED);
        payment.setFailureReason("Not paid before " + payment.getExpiresAt());
        logger.info("Payment #{} (order {}) EXPIRED", payment.getId(), payment.getRazorpayOrderId());
    }

    // ============================================================
    // REFUND / CANCEL
    // ============================================================

    /*
     * Requests a refund from Razorpay. The payment becomes REFUND_PENDING until the
     * refund.processed webhook arrives.
     */
    public void refundOnline(Payment payment, BigDecimal amount, String reason) {

        if (payment.getRazorpayPaymentId() == null) {
            throw new BadRequestException("Payment #" + payment.getId() + " has no Razorpay payment id to refund");
        }

        long paise = amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValueExact();

        try {
            JSONObject request = new JSONObject();
            request.put("amount", paise);
            JSONObject notes = new JSONObject();
            notes.put("reason", truncate(reason, 250));
            notes.put("lmsPaymentId", String.valueOf(payment.getId()));
            request.put("notes", notes);

            Refund refund = razorpayClient.payments.refund(payment.getRazorpayPaymentId(), request);
            payment.setRazorpayRefundId(refund.get("id"));

        } catch (RazorpayException e) {
            throw new PaymentGatewayException("Unable to refund payment. Please try again.", e);
        }

        payment.setStatus(PaymentStatus.REFUND_PENDING);
        payment.setRefundedAmount(amount);
        payment.setRemarks(truncate(reason, 255));

        logger.info("Refund {} requested for payment #{} amount {}",
                payment.getRazorpayRefundId(), payment.getId(), amount);
    }

    /*
     * Removes access: enrollment CANCELLED and removed from all batches
     */
    public void cancelEnrollment(Enrollment enrollment, String paymentStatusLabel) {

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollment.setPaymentStatus(paymentStatusLabel);
        enrollmentBatchRepository.deleteAll(enrollmentBatchRepository.findByEnrollmentId(enrollment.getId()));

        logger.info("Enrollment #{} cancelled ({})", enrollment.getId(), paymentStatusLabel);
    }

    // ============================================================
    // MAPPING
    // ============================================================

    public CheckoutResponse toCheckoutResponse(Payment payment) {

        Student student = payment.getStudent();

        CheckoutResponse response = new CheckoutResponse();
        response.setPaymentId(payment.getId());
        response.setStatus(payment.getStatus());
        response.setPaymentRequired(payment.getMethod() == PaymentMethod.ONLINE
                && payment.getStatus() != PaymentStatus.PAID);
        response.setEnrollmentId(payment.getEnrollment() != null ? payment.getEnrollment().getId() : null);
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setExpiresAt(payment.getExpiresAt());
        response.setStudentId(student.getStudentId());
        response.setStudentName(fullName(student));
        response.setStudentEmail(student.getEmailId());
        response.setStudentMobile(student.getMobileNum());
        response.setItemName(targetOf(payment).name());

        if (payment.getMethod() == PaymentMethod.ONLINE) {
            response.setRazorpayOrderId(payment.getRazorpayOrderId());
            response.setKeyId(keyId);
            response.setAmountInPaise(payment.getAmount().multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP).longValueExact());
        }
        return response;
    }

    public PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setStudentId(payment.getStudent().getStudentId());
        response.setStudentName(fullName(payment.getStudent()));
        response.setEnrollmentType(payment.getEnrollmentType());
        response.setCourseId(payment.getCourse() != null ? payment.getCourse().getCourseId() : null);
        response.setProgramId(payment.getProgram() != null ? payment.getProgram().getProgramId() : null);
        response.setItemName(targetOf(payment).name());
        response.setEnrollmentId(payment.getEnrollment() != null ? payment.getEnrollment().getId() : null);
        response.setMethod(payment.getMethod());
        response.setOfflineMode(payment.getOfflineMode());
        response.setStatus(payment.getStatus());
        response.setRazorpayOrderId(payment.getRazorpayOrderId());
        response.setRazorpayPaymentId(payment.getRazorpayPaymentId());
        response.setFeeAmount(payment.getFeeAmount());
        response.setDiscountAmount(payment.getDiscountAmount());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setReferenceNo(payment.getReferenceNo());
        response.setRemarks(payment.getRemarks());
        response.setFailureReason(payment.getFailureReason());
        response.setInitiatedBy(payment.getInitiatedBy());
        response.setRecordedBy(payment.getRecordedBy());
        response.setExpiresAt(payment.getExpiresAt());
        response.setPaidAt(payment.getPaidAt());
        response.setRazorpayRefundId(payment.getRazorpayRefundId());
        response.setRefundedAmount(payment.getRefundedAmount());
        response.setRefundedAt(payment.getRefundedAt());
        response.setCreatedDt(payment.getCreatedDt());
        return response;
    }

    // ============================================================
    // HELPERS
    // ============================================================

    public static String paymentStatusLabel(PaymentMethod method) {
        return switch (method) {
            case WAIVER -> "WAIVED";
            case FREE -> "FREE";
            default -> "PAID";
        };
    }

    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    /*
     * JwtFilter stores the staff id in the authentication details for staff tokens
     */
    public static Long currentStaffId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getDetails() instanceof Long id ? id : null;
    }

    public static String fullName(Student student) {
        String first = student.getFirstNm() != null ? student.getFirstNm() : "";
        String last = student.getLastNm() != null ? student.getLastNm() : "";
        return (first + " " + last).trim();
    }

    static String truncate(String value, int max) {
        return value != null && value.length() > max ? value.substring(0, max) : value;
    }

    private static boolean isSelfService(Payment payment, Student student) {
        return payment.getRecordedBy() == null
                && payment.getInitiatedBy() != null
                && payment.getInitiatedBy().equalsIgnoreCase(student.getEmailId());
    }
}
