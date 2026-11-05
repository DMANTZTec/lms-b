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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.config.SecurityUtils;
import com.dmantz.lms.dto.request.CreatePaymentOrderRequest;
import com.dmantz.lms.dto.request.VerifyPaymentRequest;
import com.dmantz.lms.dto.response.PaymentOrderResponse;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.entity.CourseFee;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.EnrollmentType;
import com.dmantz.lms.entity.Payment;
import com.dmantz.lms.entity.PaymentStatus;
import com.dmantz.lms.entity.ProgramFee;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.InvalidPaymentSignatureException;
import com.dmantz.lms.exceptions.PaymentGatewayException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.UnauthorizedAccessException;
import com.dmantz.lms.repository.CourseFeeRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.PaymentRepository;
import com.dmantz.lms.repository.ProgramFeeRepository;
import com.dmantz.lms.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    private static final String PAYMENT_STATUS_PAID = "PAID";
    private static final String PAYMENT_STATUS_FAILED = "FAILED";

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseFeeRepository courseFeeRepository;
    private final ProgramFeeRepository programFeeRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    @Value("${razorpay.webhook-secret:}")
    private String webhookSecret;

    @Value("${razorpay.currency:INR}")
    private String currency;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            CourseFeeRepository courseFeeRepository,
            ProgramFeeRepository programFeeRepository,
            RazorpayClient razorpayClient) {

        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseFeeRepository = courseFeeRepository;
        this.programFeeRepository = programFeeRepository;
        this.razorpayClient = razorpayClient;
    }

    // ============================================================
    // CREATE ORDER
    // ============================================================

    @Override
    public PaymentOrderResponse createOrder(CreatePaymentOrderRequest request) {

        Enrollment enrollment = getOwnedEnrollment(request.getEnrollmentId(), false);

        logger.info("Creating Razorpay order for enrollmentId: {}", enrollment.getId());

        if (PAYMENT_STATUS_PAID.equals(enrollment.getPaymentStatus())) {
            throw new BadRequestException("Enrollment is already paid");
        }

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new BadRequestException("Enrollment is cancelled");
        }

        // Amount is always calculated on the server, never taken from the client
        BigDecimal amount = calculateAmount(enrollment);
        long amountInPaise = amount.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        // Razorpay minimum order amount is 100 paise
        if (amountInPaise < 100) {
            throw new BadRequestException("Payable amount must be at least 1.00 " + currency);
        }

        String razorpayOrderId;
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", currency);
            // receipt is limited to 40 characters by Razorpay
            orderRequest.put("receipt", "enr_" + enrollment.getId() + "_" + System.currentTimeMillis());

            JSONObject notes = new JSONObject();
            notes.put("enrollmentId", String.valueOf(enrollment.getId()));
            notes.put("studentId", enrollment.getStudent().getStudentId());
            orderRequest.put("notes", notes);

            Order order = razorpayClient.orders.create(orderRequest);
            razorpayOrderId = order.get("id");

        } catch (RazorpayException e) {
            logger.error("Razorpay order creation failed for enrollmentId: {}", enrollment.getId(), e);
            throw new PaymentGatewayException("Unable to create payment order. Please try again.", e);
        }

        Payment payment = new Payment();
        payment.setEnrollment(enrollment);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.CREATED);
        paymentRepository.save(payment);

        logger.info("Razorpay order {} created for enrollmentId: {} amount: {}",
                razorpayOrderId, enrollment.getId(), amount);

        PaymentOrderResponse response = new PaymentOrderResponse();
        response.setRazorpayOrderId(razorpayOrderId);
        response.setKeyId(keyId);
        response.setAmount(amount);
        response.setAmountInPaise(amountInPaise);
        response.setCurrency(currency);
        response.setEnrollmentId(enrollment.getId());
        return response;
    }

    // ============================================================
    // VERIFY PAYMENT (Checkout success handler)
    // ============================================================

    @Override
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {

        logger.info("Verifying Razorpay payment for orderId: {}", request.getRazorpayOrderId());

        Payment payment = paymentRepository
                .findByRazorpayOrderIdForUpdate(request.getRazorpayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment order not found"));

        // Ownership check on the enrollment linked to this order
        assertOwner(payment.getEnrollment(), false);

        boolean valid;
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());
            valid = Utils.verifyPaymentSignature(attributes, keySecret);
        } catch (RazorpayException e) {
            valid = false;
        }

        if (!valid) {
            logger.warn("Invalid Razorpay signature for orderId: {}", request.getRazorpayOrderId());
            throw new InvalidPaymentSignatureException("Payment signature verification failed");
        }

        markPaid(payment, request.getRazorpayPaymentId(), request.getRazorpaySignature());

        return toResponse(payment);
    }

    // ============================================================
    // WEBHOOK
    // ============================================================

    @Override
    public void handleWebhook(String payload, String signature) {

        if (webhookSecret == null || webhookSecret.isBlank()) {
            logger.error("Razorpay webhook received but razorpay.webhook-secret is not configured");
            throw new BadRequestException("Webhook is not configured");
        }

        if (signature == null || signature.isBlank()) {
            throw new InvalidPaymentSignatureException("Missing webhook signature");
        }

        boolean valid;
        try {
            valid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
        } catch (RazorpayException e) {
            valid = false;
        }

        if (!valid) {
            logger.warn("Invalid Razorpay webhook signature");
            throw new InvalidPaymentSignatureException("Webhook signature verification failed");
        }

        JSONObject event = new JSONObject(payload);
        String eventType = event.optString("event");
        JSONObject eventPayload = event.optJSONObject("payload");

        JSONObject paymentEntity = entity(eventPayload, "payment");
        JSONObject orderEntity = entity(eventPayload, "order");

        String orderId = paymentEntity != null ? paymentEntity.optString("order_id", null) : null;
        if (orderId == null && orderEntity != null) {
            orderId = orderEntity.optString("id", null);
        }
        String razorpayPaymentId = paymentEntity != null ? paymentEntity.optString("id", null) : null;

        logger.info("Razorpay webhook event: {} orderId: {} paymentId: {}", eventType, orderId, razorpayPaymentId);

        if (orderId == null) {
            logger.warn("Razorpay webhook {} has no order id, ignoring", eventType);
            return;
        }

        Optional<Payment> paymentOpt = paymentRepository.findByRazorpayOrderIdForUpdate(orderId);
        if (paymentOpt.isEmpty()) {
            // Not an order created by this system; acknowledge so Razorpay stops retrying
            logger.warn("Razorpay webhook for unknown orderId: {}, ignoring", orderId);
            return;
        }

        Payment payment = paymentOpt.get();

        switch (eventType) {
            case "payment.captured", "order.paid" ->
                    markPaid(payment, razorpayPaymentId, null);

            case "payment.failed" -> {
                String reason = paymentEntity != null
                        ? paymentEntity.optString("error_description", null)
                        : null;
                markFailed(payment, razorpayPaymentId, reason);
            }

            default -> logger.info("Unhandled Razorpay webhook event: {}", eventType);
        }
    }

    // ============================================================
    // PAYMENT HISTORY
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByEnrollment(Long enrollmentId) {

        Enrollment enrollment = getOwnedEnrollment(enrollmentId, true);

        return paymentRepository
                .findByEnrollment_IdOrderByCreatedDtDescIdDesc(enrollment.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private void markPaid(Payment payment, String razorpayPaymentId, String signature) {

        if (payment.getStatus() == PaymentStatus.PAID) {
            // Idempotent: verify and webhook can both arrive for the same order
            logger.info("Payment for orderId: {} is already PAID", payment.getRazorpayOrderId());
            return;
        }

        payment.setStatus(PaymentStatus.PAID);
        if (razorpayPaymentId != null) {
            payment.setRazorpayPaymentId(razorpayPaymentId);
        }
        if (signature != null) {
            payment.setRazorpaySignature(signature);
        }
        payment.setFailureReason(null);
        payment.setPaidAt(LocalDateTime.now());

        Enrollment enrollment = payment.getEnrollment();
        enrollment.setPaymentStatus(PAYMENT_STATUS_PAID);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        logger.info("Payment PAID for orderId: {} enrollmentId: {}",
                payment.getRazorpayOrderId(), enrollment.getId());
    }

    private void markFailed(Payment payment, String razorpayPaymentId, String reason) {

        if (payment.getStatus() == PaymentStatus.PAID) {
            // A failed attempt must never override a successful one on the same order
            logger.info("Ignoring payment.failed for already PAID orderId: {}", payment.getRazorpayOrderId());
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);
        if (razorpayPaymentId != null) {
            payment.setRazorpayPaymentId(razorpayPaymentId);
        }
        if (reason != null && reason.length() > 500) {
            reason = reason.substring(0, 500);
        }
        payment.setFailureReason(reason);

        Enrollment enrollment = payment.getEnrollment();
        if (!PAYMENT_STATUS_PAID.equals(enrollment.getPaymentStatus())) {
            enrollment.setPaymentStatus(PAYMENT_STATUS_FAILED);
        }

        logger.info("Payment FAILED for orderId: {} reason: {}", payment.getRazorpayOrderId(), reason);
    }

    private BigDecimal calculateAmount(Enrollment enrollment) {

        LocalDate today = LocalDate.now();
        BigDecimal fee;
        BigDecimal discount;

        if (enrollment.getEnrollmentType() == EnrollmentType.COURSE) {
            CourseFee courseFee = courseFeeRepository
                    .findTopByCourse_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                            enrollment.getCourse().getId(), today)
                    .orElseThrow(() -> new ResourceNotFoundException("No fee configured for this course"));
            fee = courseFee.getFee();
            discount = courseFee.getDiscount();

        } else {
            ProgramFee programFee = programFeeRepository
                    .findTopByProgram_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                            enrollment.getProgram().getId(), today)
                    .orElseThrow(() -> new ResourceNotFoundException("No fee configured for this program"));
            fee = programFee.getFee();
            discount = programFee.getDiscount();
        }

        BigDecimal amount = fee.subtract(discount != null ? discount : BigDecimal.ZERO);
        return amount.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    private Enrollment getOwnedEnrollment(Long enrollmentId, boolean allowStaff) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        assertOwner(enrollment, allowStaff);
        return enrollment;
    }

    /**
     * Access rules:
     *  - ADMIN: always allowed (can create/verify payments on behalf of a student and view history)
     *  - STAFF: allowed only when allowStaff is true (read-only payment history)
     *  - STUDENT: allowed only for their own enrollment
     */
    private void assertOwner(Enrollment enrollment, boolean allowStaff) {

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Admin {} accessing payment for enrollmentId: {}",
                    SecurityUtils.getCurrentUser(), enrollment.getId());
            return;
        }

        if (allowStaff && hasRole("ROLE_STAFF")) {
            return;
        }

        String currentUser = SecurityUtils.getCurrentUser();
        String ownerEmail = enrollment.getStudent().getEmailId();

        if (currentUser == null || ownerEmail == null || !ownerEmail.equalsIgnoreCase(currentUser)) {
            logger.warn("User {} attempted to access enrollmentId: {} they do not own",
                    currentUser, enrollment.getId());
            throw new UnauthorizedAccessException("You are not allowed to access this enrollment");
        }
    }

    private boolean hasRole(String role) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    private static JSONObject entity(JSONObject eventPayload, String key) {

        if (eventPayload == null) {
            return null;
        }
        JSONObject wrapper = eventPayload.optJSONObject(key);
        return wrapper != null ? wrapper.optJSONObject("entity") : null;
    }

    private PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setEnrollmentId(payment.getEnrollment().getId());
        response.setRazorpayOrderId(payment.getRazorpayOrderId());
        response.setRazorpayPaymentId(payment.getRazorpayPaymentId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus());
        response.setFailureReason(payment.getFailureReason());
        response.setPaidAt(payment.getPaidAt());
        response.setCreatedDt(payment.getCreatedDt());
        return response;
    }
}
