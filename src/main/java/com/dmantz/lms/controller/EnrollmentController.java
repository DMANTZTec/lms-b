package com.dmantz.lms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms.dto.response.EnrollmentResponse;
import com.dmantz.lms.service.EnrollmentService;

/*
 * Read and cancel enrollments. Enrollments are created only through payment:
 *   student -> POST /api/checkout, admin -> POST /api/admin/enrollments
 */
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(
            EnrollmentService enrollmentService) {

        this.enrollmentService = enrollmentService;
    }


    // GET ALL

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>>
            getAllEnrollments() {

        return ResponseEntity.ok(
                enrollmentService.getAllEnrollments()
        );
    }


    // GET BY ID

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse>
            getEnrollmentById(
                    @PathVariable Long id) {

        return ResponseEntity.ok(
                enrollmentService.getEnrollmentById(id)
        );
    }


    // GET BY STUDENT

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>>
            getEnrollmentsByStudent(
                    @PathVariable String studentId) {

        return ResponseEntity.ok(
                enrollmentService
                        .getEnrollmentsByStudent(studentId)
        );
    }


    // CANCEL (admin; no refund. Use POST /api/admin/payments/{id}/refund to return money)

    @DeleteMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> cancelEnrollment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                enrollmentService.cancelEnrollment(id)
        );
    }
}
