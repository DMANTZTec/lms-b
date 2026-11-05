package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.CheckoutRequest;
import com.dmantz.lms.dto.request.VerifyPaymentRequest;
import com.dmantz.lms.dto.response.CheckoutResponse;
import com.dmantz.lms.dto.response.PaymentResponse;

public interface PaymentService {

    // Student starts paying for a course / program (no enrollment is created yet)
    CheckoutResponse checkout(CheckoutRequest request);

    // Razorpay Checkout success handler -> payment PAID -> enrollment created
    PaymentResponse verifyPayment(VerifyPaymentRequest request);

    // Razorpay webhook (source of truth)
    void handleWebhook(String payload, String signature);

    List<PaymentResponse> getPaymentsByEnrollment(Long enrollmentId);

    // Logged-in student's payments, including pending requests sent by admin
    List<PaymentResponse> getMyPayments();
}
