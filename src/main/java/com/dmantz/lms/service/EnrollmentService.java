package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.EnrollmentRequest;
import com.dmantz.lms.dto.response.EnrollmentResponse;

public interface EnrollmentService {

    EnrollmentResponse createEnrollment(
            EnrollmentRequest request
    );

    List<EnrollmentResponse> getAllEnrollments();

    EnrollmentResponse getEnrollmentById(
            Long id
    );

    List<EnrollmentResponse> getEnrollmentsByStudent(
            String studentId
    );

    EnrollmentResponse updateEnrollment(
            Long id,
            EnrollmentRequest request
    );

    void deleteEnrollment(
            Long id
    );
}