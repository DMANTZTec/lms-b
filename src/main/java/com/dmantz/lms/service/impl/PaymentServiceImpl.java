package com.dmantz.lms.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.config.SecurityUtils;
import com.dmantz.lms.dto.request.CheckoutRequest;
import com.dmantz.lms.dto.request.VerifyPaymentRequest;
import com.dmantz.lms.dto.response.CheckoutResponse;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.Payment;
import com.dmantz.lms.entity.PaymentStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.InvalidPaymentSignatureException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.UnauthorizedAccessException;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.PaymentRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.PaymentService;
import com.dmantz.lms.service.impl.PaymentWorkflow.FeeQuote;
import com.dmantz.lms.service.impl.PaymentWorkflow.PurchaseTarget;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    private final PaymentWorkflow workflow;
    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    @Value("${razorpay.webhook-secret:}")
    private String webhookSecret;

    @Value("${payment.student-order-expiry-hours:24}")
    private int studentOrderExpiryHours;

    public PaymentServiceImpl(
            PaymentWorkflow workflow,
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository) {

        this.workflow = workflow;
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    // ============================================================
    // CHECKOUT (student)
    // ============================================================

    @Override
    public CheckoutResponse checkout(CheckoutRequest request) {

        // Student always comes from the token, never from the request body
        Student me = studentRepository.findByEmailId(SecurityUtils.getCurrentUser())
                .orElseThrow(() -> new UnauthorizedAccessException("Only students can use checkout"));

        Student student = workflow.lockStudent(me.getStudentId());
        PurchaseTarget target = workflow.resolveTarget(request.getCourseId(), request.getProgramId());

        logger.info("Checkout: student {} {} {}", student.getStudentId(), target.type(), target.id());

        workflow.assertNotEnrolled(student, target);

        FeeQuote quote = workflow.quote(target);

        // Free: enroll immediately, nothing to pay
        if (quote.isFree()) {
            return workflow.toCheckoutResponse(workflow.settleFree(student, target, quote));
        }

        // Reuse an unpaid order for the same amount (retry, or a request sent by admin)
        Optional<Payment> open = workflow.findOpenRequest(student, target);
        if (open.isPresent() && open.get().getAmount().compareTo(quote.payable()) == 0) {
            logger.info("Reusing open payment #{} (order {})", open.get().getId(), open.get().getRazorpayOrderId());
            return workflow.toCheckoutResponse(open.get());
        }

        Payment payment = workflow.createOnlineRequest(student, target, quote, studentOrderExpiryHours);
        return workflow.toCheckoutResponse(payment);
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

        assertCanAccess(payment.getStudent(), false);

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

        workflow.markOnlinePaid(payment, request.getRazorpayPaymentId(), request.getRazorpaySignature());

        return workflow.toResponse(payment);
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
        JSONObject refundEntity = entity(eventPayload, "refund");

        logger.info("Razorpay webhook event: {}", eventType);

        switch (eventType) {
            case "payment.captured", "order.paid", "payment.failed" ->
                    handlePaymentEvent(eventType, paymentEntity, orderEntity);

            case "refund.processed", "refund.failed" ->
                    handleRefundEvent(eventType, refundEntity, paymentEntity);

            default -> logger.info("Unhandled Razorpay webhook event: {}", eventType);
        }
    }

    private void handlePaymentEvent(String eventType, JSONObject paymentEntity, JSONObject orderEntity) {

        String orderId = paymentEntity != null ? paymentEntity.optString("order_id", null) : null;
        if (orderId == null && orderEntity != null) {
            orderId = orderEntity.optString("id", null);
        }
        String razorpayPaymentId = paymentEntity != null ? paymentEntity.optString("id", null) : null;

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

        if ("payment.failed".equals(eventType)) {
            String reason = paymentEntity != null ? paymentEntity.optString("error_description", null) : null;
            workflow.markOnlineFailed(payment, razorpayPaymentId, reason);
        } else {
            workflow.markOnlinePaid(payment, razorpayPaymentId, null);
        }
    }

    private void handleRefundEvent(String eventType, JSONObject refundEntity, JSONObject paymentEntity) {

        String razorpayPaymentId = refundEntity != null ? refundEntity.optString("payment_id", null) : null;
        if (razorpayPaymentId == null && paymentEntity != null) {
            razorpayPaymentId = paymentEntity.optString("id", null);
        }
        if (razorpayPaymentId == null) {
            logger.warn("Razorpay webhook {} has no payment id, ignoring", eventType);
            return;
        }

        Optional<Payment> paymentOpt = paymentRepository.findByRazorpayPaymentIdForUpdate(razorpayPaymentId);
        if (paymentOpt.isEmpty()) {
            logger.warn("Razorpay webhook {} for unknown paymentId: {}, ignoring", eventType, razorpayPaymentId);
            return;
        }

        Payment payment = paymentOpt.get();

        if ("refund.processed".equals(eventType)) {
            if (payment.getStatus() == PaymentStatus.REFUNDED) {
                return;
            }
            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setRefundedAt(LocalDateTime.now());
            if (paymentEntity != null && paymentEntity.has("amount_refunded")) {
                payment.setRefundedAmount(BigDecimal.valueOf(paymentEntity.getLong("amount_refunded"))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            }
            logger.info("Refund processed for payment #{}", payment.getId());
        } else {
            // Refund failed: money is still with us; admin can retry the refund
            if (payment.getStatus() == PaymentStatus.REFUND_PENDING) {
                payment.setStatus(PaymentStatus.PAID);
            }
            payment.setFailureReason("Refund failed at Razorpay, please retry");
            logger.error("Refund FAILED for payment #{}", payment.getId());
        }
    }

    // ============================================================
    // PAYMENT HISTORY
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByEnrollment(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        assertCanAccess(enrollment.getStudent(), true);

        return paymentRepository
                .findByEnrollment_IdOrderByCreatedDtDescIdDesc(enrollment.getId())
                .stream()
                .map(workflow::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getMyPayments() {

        Student me = studentRepository.findByEmailId(SecurityUtils.getCurrentUser())
                .orElseThrow(() -> new UnauthorizedAccessException("Only students can view their payments"));

        return paymentRepository.findByStudent_StudentIdOrderByIdDesc(me.getStudentId())
                .stream()
                .map(workflow::toResponse)
                .toList();
    }

    // ============================================================
    // HELPERS
    // ============================================================

    /*
     * ADMIN: always allowed
     * STAFF: allowed only when allowStaff is true (read-only history)
     * STUDENT: only their own payments
     */
    private void assertCanAccess(Student owner, boolean allowStaff) {

        if (PaymentWorkflow.hasRole("ROLE_ADMIN")) {
            return;
        }
        if (allowStaff && PaymentWorkflow.hasRole("ROLE_STAFF")) {
            return;
        }

        String currentUser = SecurityUtils.getCurrentUser();
        String ownerEmail = owner.getEmailId();

        if (currentUser == null || ownerEmail == null || !ownerEmail.equalsIgnoreCase(currentUser)) {
            logger.warn("User {} attempted to access payments of student {}", currentUser, owner.getStudentId());
            throw new UnauthorizedAccessException("You are not allowed to access this payment");
        }
    }

    private static JSONObject entity(JSONObject eventPayload, String key) {

        if (eventPayload == null) {
            return null;
        }
        JSONObject wrapper = eventPayload.optJSONObject(key);
        return wrapper != null ? wrapper.optJSONObject("entity") : null;
    }
}
