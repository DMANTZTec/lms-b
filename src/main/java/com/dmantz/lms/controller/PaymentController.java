package com.dmantz.lms.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.VerifyPaymentRequest;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LogManager.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    // VERIFY CHECKOUT RESULT

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request) {

        logger.info("Received verify payment request for orderId: {}", request.getRazorpayOrderId());

        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }


    // RAZORPAY WEBHOOK (public, authenticated by X-Razorpay-Signature)

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {

        // Raw body is required: the HMAC is computed over the exact bytes Razorpay sent
        paymentService.handleWebhook(payload, signature);

        return ResponseEntity.ok().build();
    }


    // PAYMENT HISTORY FOR AN ENROLLMENT

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByEnrollment(
            @PathVariable Long enrollmentId) {

        return ResponseEntity.ok(paymentService.getPaymentsByEnrollment(enrollmentId));
    }


    // LOGGED-IN STUDENT'S PAYMENTS (incl. pending requests from admin)

    @GetMapping("/me")
    public ResponseEntity<List<PaymentResponse>> getMyPayments() {

        return ResponseEntity.ok(paymentService.getMyPayments());
    }
}
