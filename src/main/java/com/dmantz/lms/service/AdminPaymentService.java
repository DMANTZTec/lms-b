package com.dmantz.lms.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dmantz.lms.dto.request.AdminEnrollmentRequest;
import com.dmantz.lms.dto.request.OfflinePaymentRequest;
import com.dmantz.lms.dto.request.PaymentActionRequest;
import com.dmantz.lms.dto.request.RefundRequest;
import com.dmantz.lms.dto.response.AdminEnrollmentResponse;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.entity.PaymentMethod;
import com.dmantz.lms.entity.PaymentStatus;

public interface AdminPaymentService {

    // Enroll a student: cash received, waiver, pay later or collect online
    AdminEnrollmentResponse enroll(AdminEnrollmentRequest request);

    // Student with a pending online request paid at the office instead
    AdminEnrollmentResponse recordOfflinePayment(Long paymentId, OfflinePaymentRequest request);

    // Waive the fee for a pending online request
    AdminEnrollmentResponse waive(Long paymentId, PaymentActionRequest request);

    // Cancel a pending online request
    PaymentResponse cancelRequest(Long paymentId, PaymentActionRequest request);

    // Full or partial refund of a PAID payment; a full refund cancels the enrollment
    PaymentResponse refund(Long paymentId, RefundRequest request);

    // Reports and cash reconciliation
    Page<PaymentResponse> search(PaymentMethod method, PaymentStatus status, String studentId,
                                 Long recordedBy, LocalDate from, LocalDate to, Pageable pageable);
}
