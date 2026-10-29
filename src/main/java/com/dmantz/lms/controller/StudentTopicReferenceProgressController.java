package com.dmantz.lms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms.dto.response.StudentTopicReferenceProgressResponse;
import com.dmantz.lms.service.StudentTopicReferenceProgressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/progress")
public class StudentTopicReferenceProgressController {

	private static final Logger logger = LogManager.getLogger(StudentTopicReferenceProgressController.class);

	private final StudentTopicReferenceProgressService studentTopicReferenceProgressService;

	public StudentTopicReferenceProgressController(
			StudentTopicReferenceProgressService studentTopicReferenceProgressService) {

		this.studentTopicReferenceProgressService = studentTopicReferenceProgressService;
	}

	@PostMapping("/markascomplete")
	public ResponseEntity<StudentTopicReferenceProgressResponse> markReferenceCompleted(
			@Valid @RequestBody StudentTopicReferenceProgressRequest request) {

		logger.info("Received mark reference complete request for studentId: {} and referenceId: {}",
				request.getStudentId(), request.getReferenceId());

		StudentTopicReferenceProgressResponse response = studentTopicReferenceProgressService
				.markReferenceComplete(request);

		logger.info("Reference marked completed successfully for studentId: {} and referenceId: {}",
				request.getStudentId(), request.getReferenceId());

		return ResponseEntity.ok(response);
	}
}