package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.AssignStudentToBatchRequest;
import com.dmantz.lms.dto.response.EnrollmentBatchResponse;
import com.dmantz.lms.dto.response.StudentWeeklyScheduleResponse;
import com.dmantz.lms.service.EnrollmentBatchService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/enrollment-batches")
public class EnrollmentBatchController {

    private final EnrollmentBatchService service;


    public EnrollmentBatchController(
            EnrollmentBatchService service
    ) {

        this.service = service;
    }


    // ==========================================================
    // 1. ASSIGN STUDENT TO BATCH
    // ==========================================================

    @PostMapping("/assign")
    public ResponseEntity<EnrollmentBatchResponse>
    assignStudentToBatch(
            @Valid
            @RequestBody
            AssignStudentToBatchRequest request
    ) {

        EnrollmentBatchResponse response =
                service.assignStudentToBatch(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ==========================================================
    // 2. GET STUDENTS IN BATCH
    // ==========================================================

    @GetMapping(
            "/batches/{batchId}/students"
    )
    public ResponseEntity<
            List<EnrollmentBatchResponse>>
    getStudentsByBatch(
            @PathVariable Long batchId
    ) {

        return ResponseEntity.ok(
                service.getStudentsByBatch(
                        batchId
                )
        );
    }


    // ==========================================================
    // 3. GET ENROLLMENT BATCH BY ID
    // ==========================================================

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentBatchResponse>
    getEnrollmentBatch(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getEnrollmentBatch(id)
        );
    }


    // ==========================================================
    // 4. REMOVE STUDENT FROM BATCH
    // ==========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    removeStudentFromBatch(
            @PathVariable Long id
    ) {

        service.removeStudentFromBatch(id);

        return ResponseEntity.noContent()
                .build();
    }


    // ==========================================================
    // 5. STUDENT WEEKLY SCHEDULE
    // ==========================================================

    @GetMapping(
            "/students/{studentId}/weekly-schedule"
    )
    public ResponseEntity<
            List<StudentWeeklyScheduleResponse>>
    getStudentWeeklySchedule(
            @PathVariable String studentId,

            @RequestParam
            LocalDate startDate
    ) {

        return ResponseEntity.ok(
                service.getStudentWeeklySchedule(
                        studentId,
                        startDate
                )
        );
    }
}