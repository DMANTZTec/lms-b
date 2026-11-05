package com.dmantz.lms.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.AdminEnrollmentRequest;
import com.dmantz.lms.dto.request.OfflinePaymentRequest;
import com.dmantz.lms.dto.request.PaymentActionRequest;
import com.dmantz.lms.dto.request.RefundRequest;
import com.dmantz.lms.dto.response.AdminEnrollmentResponse;
import com.dmantz.lms.dto.response.PaymentResponse;
import com.dmantz.lms.entity.PaymentMethod;
import com.dmantz.lms.entity.PaymentStatus;
import com.dmantz.lms.service.AdminPaymentService;

import jakarta.validation.Valid;

/*
 * ADMIN only (see SecurityConfig): enroll students with cash / waiver / pay later / online,
 * manage pending payment requests, refunds and payment reports.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    public AdminPaymentController(AdminPaymentService adminPaymentService) {
        this.adminPaymentService = adminPaymentService;
    }

    // ENROLL A STUDENT

    @PostMapping("/enrollments")
    public ResponseEntity<AdminEnrollmentResponse> enroll(@Valid @RequestBody AdminEnrollmentRequest request) {

        AdminEnrollmentResponse response = adminPaymentService.enroll(request);

        // 201 when the enrollment was created now; 200 when it waits for an online payment
        return ResponseEntity
                .status(response.getEnrollment() != null ? HttpStatus.CREATED : HttpStatus.OK)
                .body(response);
    }

    // PENDING REQUEST PAID AT THE OFFICE

    @PostMapping("/payments/{paymentId}/offline")
    public ResponseEntity<AdminEnrollmentResponse> recordOfflinePayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody OfflinePaymentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminPaymentService.recordOfflinePayment(paymentId, request));
    }

    // WAIVE FEE FOR A PENDING REQUEST

    @PostMapping("/payments/{paymentId}/waive")
    public ResponseEntity<AdminEnrollmentResponse> waive(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentActionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminPaymentService.waive(paymentId, request));
    }

    // CANCEL A PENDING REQUEST

    @PostMapping("/payments/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelRequest(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentActionRequest request) {

        return ResponseEntity.ok(adminPaymentService.cancelRequest(paymentId, request));
    }

    // REFUND

    @PostMapping("/payments/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refund(
            @PathVariable Long paymentId,
            @Valid @RequestBody RefundRequest request) {

        return ResponseEntity.ok(adminPaymentService.refund(paymentId, request));
    }

    // REPORT / CASH RECONCILIATION

    @GetMapping("/payments")
    public ResponseEntity<Page<PaymentResponse>> search(
            @RequestParam(required = false) PaymentMethod method,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Long recordedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(adminPaymentService.search(method, status, studentId, recordedBy, from, to,
                PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "id"))));
    }
}
