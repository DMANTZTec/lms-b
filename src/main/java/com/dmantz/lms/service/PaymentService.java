package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.CreatePaymentOrderRequest;
import com.dmantz.lms.dto.request.VerifyPaymentRequest;
import com.dmantz.lms.dto.response.PaymentOrderResponse;
import com.dmantz.lms.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentOrderResponse createOrder(
            CreatePaymentOrderRequest request
    );

    PaymentResponse verifyPayment(
            VerifyPaymentRequest request
    );

    void handleWebhook(
            String payload,
            String signature
    );

    List<PaymentResponse> getPaymentsByEnrollment(
            Long enrollmentId
    );
}
