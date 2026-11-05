package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.response.EnrollmentResponse;

/*
 * Enrollments are created only through payment:
 *   student -> POST /api/checkout, admin -> POST /api/admin/enrollments
 */
public interface EnrollmentService {

    List<EnrollmentResponse> getAllEnrollments();

    EnrollmentResponse getEnrollmentById(
            Long id
    );

    List<EnrollmentResponse> getEnrollmentsByStudent(
            String studentId
    );

    // Admin cancels without refund (use the refund API to return money)
    EnrollmentResponse cancelEnrollment(
            Long id
    );
}
