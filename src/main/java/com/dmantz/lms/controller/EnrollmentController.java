package com.dmantz.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms.dto.request.EnrollmentRequest;
import com.dmantz.lms.dto.response.EnrollmentResponse;
import com.dmantz.lms.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(
            EnrollmentService enrollmentService) {

        this.enrollmentService = enrollmentService;
    }


    // CREATE

    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @RequestBody EnrollmentRequest request) {

        EnrollmentResponse response =
                enrollmentService.createEnrollment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
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


    // UPDATE

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponse>
            updateEnrollment(
                    @PathVariable Long id,
                    @RequestBody EnrollmentRequest request) {

        return ResponseEntity.ok(
                enrollmentService
                        .updateEnrollment(id, request)
        );
    }


    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long id) {

        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.noContent().build();
    }
}