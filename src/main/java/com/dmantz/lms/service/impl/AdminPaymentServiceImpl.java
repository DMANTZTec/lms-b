package com.dmantz.lms.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.AdminEnrollmentRequest;
import com.dmantz.lms.dto.request.OfflinePaymentRequest;
import com.dmantz.lms.dto.request.PaymentActionRequest;
import com.dmantz.lms.dto.request.RefundRequest;
import com.dmantz.lms.dto.response.AdminEnrollmentResponse;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.OfflinePaymentMode;
import com.dmantz.lms.entity.Payment;
import com.dmantz.lms.entity.PaymentMethod;
import com.dmantz.lms.entity.PaymentStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.EnrollmentMapper;
import com.dmantz.lms.repository.PaymentRepository;
import com.dmantz.lms.service.AdminPaymentService;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.impl.PaymentWorkflow.FeeQuote;
import com.dmantz.lms.service.impl.PaymentWorkflow.PurchaseTarget;

@Service
@Transactional
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private static final Logger logger = LogManager.getLogger(AdminPaymentServiceImpl.class);

    private final PaymentWorkflow workflow;
    private final PaymentRepository paymentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final EmailService emailService;

    @Value("${payment.student-order-expiry-hours:24}")
    private int studentOrderExpiryHours;

    @Value("${payment.admin-request-expiry-hours:72}")
    private int adminRequestExpiryHours;

    public AdminPaymentServiceImpl(
            PaymentWorkflow workflow,
            PaymentRepository paymentRepository,
            EnrollmentMapper enrollmentMapper,
            EmailService emailService) {

        this.workflow = workflow;
        this.paymentRepository = paymentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.emailService = emailService;
    }

    // ============================================================
    // ENROLL
    // ============================================================

    @Override
    public AdminEnrollmentResponse enroll(AdminEnrollmentRequest request) {

        Student student = workflow.lockStudent(request.getStudentId().trim());
        PurchaseTarget target = workflow.resolveTarget(request.getCourseId(), request.getProgramId());

        logger.info("Admin {} enrolling student {} in {} {} with option {}",
                PaymentWorkflow.currentStaffId(), student.getStudentId(), target.type(), target.id(),
                request.getPaymentOption());

        workflow.assertNotEnrolled(student, target);

        FeeQuote quote = workflow.quote(target);

        // Nothing to collect, whatever option was chosen
        if (quote.isFree()) {
            return settled(workflow.settleFree(student, target, quote));
        }

        return switch (request.getPaymentOption()) {

            case CASH_RECEIVED -> settled(settleOffline(student, target, quote, request.getOfflinePayment()));

            case WAIVER -> settled(settleWaiver(student, target, quote, request.getWaiverReason()));

            case PAY_LATER -> {
                Payment payment = openOrCreateRequest(student, target, quote, adminRequestExpiryHours);
                sendPaymentRequestEmail(student, target, payment);
                yield pending(payment);
            }

            case COLLECT_ONLINE -> pending(openOrCreateRequest(student, target, quote, studentOrderExpiryHours));
        };
    }

    // ============================================================
    // ACTIONS ON AN EXISTING PENDING REQUEST
    // ============================================================

    @Override
    public AdminEnrollmentResponse recordOfflinePayment(Long paymentId, OfflinePaymentRequest request) {

        Payment pendingRequest = lockOpenRequest(paymentId);
        Student student = workflow.lockStudent(pendingRequest.getStudent().getStudentId());
        PurchaseTarget target = workflow.targetOf(pendingRequest);

        workflow.assertNotEnrolled(student, target);

        // Settle at the current fee; the old online order is void
        Payment offline = settleOffline(student, target, workflow.quote(target), request);
        voidRequest(pendingRequest, "Paid offline, see payment #" + offline.getId());

        return settled(offline);
    }

    @Override
    public AdminEnrollmentResponse waive(Long paymentId, PaymentActionRequest request) {

        Payment pendingRequest = lockOpenRequest(paymentId);
        Student student = workflow.lockStudent(pendingRequest.getStudent().getStudentId());
        PurchaseTarget target = workflow.targetOf(pendingRequest);

        workflow.assertNotEnrolled(student, target);

        Payment waiver = settleWaiver(student, target, workflow.quote(target), request.getReason());
        voidRequest(pendingRequest, "Fee waived, see payment #" + waiver.getId());

        return settled(waiver);
    }

    @Override
    public PaymentResponse cancelRequest(Long paymentId, PaymentActionRequest request) {

        Payment pendingRequest = lockOpenRequest(paymentId);
        voidRequest(pendingRequest, "Cancelled by admin: " + request.getReason());

        logger.info("Admin {} cancelled payment request #{}", PaymentWorkflow.currentStaffId(), paymentId);
        return workflow.toResponse(pendingRequest);
    }

    // ============================================================
    // REFUND
    // ============================================================

    @Override
    public PaymentResponse refund(Long paymentId, RefundRequest request) {

        Payment payment = paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new BadRequestException("Only PAID payments can be refunded. Payment #" + paymentId
                    + " is " + payment.getStatus());
        }
        if (payment.getAmount().signum() <= 0) {
            throw new BadRequestException("Payment #" + paymentId + " has no amount to refund ("
                    + payment.getMethod() + "). Cancel the enrollment instead.");
        }

        BigDecimal amount = request.getAmount() != null ? request.getAmount() : payment.getAmount();
        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new BadRequestException("Refund amount cannot exceed the paid amount of " + payment.getAmount());
        }
        boolean fullRefund = amount.compareTo(payment.getAmount()) == 0;
        String reason = "Refund by admin: " + request.getReason();

        if (payment.getMethod() == PaymentMethod.ONLINE) {
            // REFUND_PENDING until the refund.processed webhook arrives
            workflow.refundOnline(payment, amount, reason);
        } else {
            // Offline money is returned outside the system; record it as done
            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setRefundedAmount(amount);
            payment.setRefundedAt(LocalDateTime.now());
            payment.setRemarks(PaymentWorkflow.truncate(reason, 255));
        }

        // A full refund removes access immediately
        Enrollment enrollment = payment.getEnrollment();
        if (fullRefund && enrollment != null) {
            workflow.cancelEnrollment(enrollment, "REFUNDED");
        }

        logger.info("Admin {} refunded {} of payment #{} ({}), full refund: {}",
                PaymentWorkflow.currentStaffId(), amount, paymentId, payment.getMethod(), fullRefund);

        return workflow.toResponse(payment);
    }

    // ============================================================
    // REPORT
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> search(PaymentMethod method, PaymentStatus status, String studentId,
                                        Long recordedBy, LocalDate from, LocalDate to, Pageable pageable) {

        return paymentRepository.search(
                        method,
                        status,
                        studentId != null && !studentId.isBlank() ? studentId.trim() : null,
                        recordedBy,
                        from != null ? from.atStartOfDay() : null,
                        to != null ? to.plusDays(1).atStartOfDay() : null,
                        pageable)
                .map(workflow::toResponse);
    }

    // ============================================================
    // HELPERS
    // ============================================================

    /*
     * Money collected by admin (cash / bank transfer / cheque / UPI): PAID + enrollment, one transaction
     */
    private Payment settleOffline(Student student, PurchaseTarget target, FeeQuote quote,
                                  OfflinePaymentRequest offline) {

        if (offline == null) {
            throw new BadRequestException("offlinePayment is required when paymentOption is CASH_RECEIVED");
        }

        if (offline.getAmount().compareTo(quote.payable()) != 0) {
            throw new BadRequestException("Amount received must equal the payable fee of " + quote.payable()
                    + ". Use WAIVER with a reason to give a concession.");
        }

        String referenceNo = offline.getReferenceNo() != null ? offline.getReferenceNo().trim() : "";
        if (referenceNo.isEmpty() && offline.getMode() != OfflinePaymentMode.CASH) {
            throw new BadRequestException("referenceNo is required for " + offline.getMode()
                    + " (UTR / cheque / UPI transaction number)");
        }
        if (!referenceNo.isEmpty() && paymentRepository.existsByReferenceNo(referenceNo)) {
            throw new DuplicateValuesException("Reference number " + referenceNo + " is already recorded");
        }

        Payment payment = workflow.newPayment(student, target, quote, PaymentMethod.OFFLINE, PaymentStatus.PAID);
        payment.setOfflineMode(offline.getMode());
        payment.setReferenceNo(referenceNo.isEmpty() ? null : referenceNo);
        payment.setRemarks(offline.getRemarks());
        payment.setRecordedBy(PaymentWorkflow.currentStaffId());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Cash without a receipt number: generate one from the payment id
        if (payment.getReferenceNo() == null) {
            payment.setReferenceNo(String.format("RCPT-%d-%06d", LocalDate.now().getYear(), payment.getId()));
        }

        workflow.settle(payment);

        logger.info("Offline payment #{} recorded: {} {} ref {} by staff {}",
                payment.getId(), offline.getMode(), payment.getAmount(), payment.getReferenceNo(),
                payment.getRecordedBy());

        return payment;
    }

    /*
     * Fee waived: PAID with amount 0, reason required, + enrollment
     */
    private Payment settleWaiver(Student student, PurchaseTarget target, FeeQuote quote, String reason) {

        if (reason == null || reason.isBlank()) {
            throw new BadRequestException("waiverReason is required when paymentOption is WAIVER");
        }

        Payment payment = workflow.newPayment(student, target, quote, PaymentMethod.WAIVER, PaymentStatus.PAID);
        payment.setAmount(BigDecimal.ZERO.setScale(2));
        payment.setRemarks(PaymentWorkflow.truncate(reason.trim(), 255));
        payment.setRecordedBy(PaymentWorkflow.currentStaffId());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        workflow.settle(payment);

        logger.info("Fee waived: payment #{} student {} {} reason '{}' by staff {}",
                payment.getId(), student.getStudentId(), target.id(), payment.getRemarks(), payment.getRecordedBy());

        return payment;
    }

    private Payment openOrCreateRequest(Student student, PurchaseTarget target, FeeQuote quote, int expiryHours) {

        Optional<Payment> open = workflow.findOpenRequest(student, target);
        if (open.isPresent() && open.get().getAmount().compareTo(quote.payable()) == 0) {
            return open.get();
        }
        return workflow.createOnlineRequest(student, target, quote, expiryHours);
    }

    private Payment lockOpenRequest(Long paymentId) {

        Payment payment = paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        boolean open = payment.getMethod() == PaymentMethod.ONLINE
                && (payment.getStatus() == PaymentStatus.CREATED
                    || payment.getStatus() == PaymentStatus.FAILED
                    || payment.getStatus() == PaymentStatus.EXPIRED);

        if (!open) {
            throw new BadRequestException("Payment #" + paymentId + " is not a pending online request ("
                    + payment.getMethod() + " / " + payment.getStatus() + ")");
        }
        return payment;
    }

    /*
     * The Razorpay order may still be paid later; settle() then refunds it as a duplicate.
     */
    private void voidRequest(Payment request, String remarks) {
        request.setStatus(PaymentStatus.CANCELLED);
        request.setRemarks(PaymentWorkflow.truncate(remarks, 255));
    }

    private AdminEnrollmentResponse settled(Payment payment) {

        AdminEnrollmentResponse response = new AdminEnrollmentResponse();
        response.setPayment(workflow.toResponse(payment));
        if (payment.getEnrollment() != null) {
            response.setEnrollment(enrollmentMapper.toResponse(payment.getEnrollment()));
        }
        return response;
    }

    private AdminEnrollmentResponse pending(Payment payment) {

        AdminEnrollmentResponse response = new AdminEnrollmentResponse();
        response.setPayment(workflow.toResponse(payment));
        response.setCheckout(workflow.toCheckoutResponse(payment));
        return response;
    }

    private void sendPaymentRequestEmail(Student student, PurchaseTarget target, Payment payment) {

        // The request is saved even if the email fails; admin can share the details another way
        try {
            emailService.sendPaymentRequestEmail(student.getEmailId(), PaymentWorkflow.fullName(student),
                    target.name(), payment.getAmount(), payment.getExpiresAt());
        } catch (RuntimeException e) {
            logger.error("Payment request email failed for student {} payment #{}",
                    student.getStudentId(), payment.getId(), e);
        }
    }
}
