package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.StudentTaskSubmissionRequest;
import com.dmantz.lms.dto.response.StudentTaskSubmissionResponse;
import com.dmantz.lms.service.StudentTaskSubmissionService;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student-task-submission")
public class StudentTaskSubmissionController {

    private static final Logger logger = LogManager.getLogger(StudentTaskSubmissionController.class);

    private final StudentTaskSubmissionService submissionService;

    public StudentTaskSubmissionController(StudentTaskSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // ================= SUBMIT TASK FOR REVIEW =================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentTaskSubmissionResponse> submitTask(
            @Valid @ModelAttribute StudentTaskSubmissionRequest request) throws Exception {

        logger.info("Received submit task request for studentId: {} taskId: {}",
                request.getStudentId(), request.getStudentTaskId());

        StudentTaskSubmissionResponse response = submissionService.submitTask(request);

        return ResponseEntity.ok(response);
    }
}