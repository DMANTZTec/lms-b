package com.dmantz.lms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.CheckoutRequest;
import com.dmantz.lms.dto.response.CheckoutResponse;
import com.dmantz.lms.service.PaymentService;

/*
 * Student checkout: creates (or reuses) a Razorpay order for a course / program.
 * The enrollment is created only after the payment succeeds (verify or webhook).
 * For a free course the enrollment is created immediately and paymentRequired is false.
 */
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private static final Logger logger = LogManager.getLogger(CheckoutController.class);

    private final PaymentService paymentService;

    public CheckoutController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {

        logger.info("Checkout request courseId: {} programId: {}", request.getCourseId(), request.getProgramId());

        return ResponseEntity.ok(paymentService.checkout(request));
    }
}
