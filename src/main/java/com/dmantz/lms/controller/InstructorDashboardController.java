package com.dmantz.lms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.dto.response.InstructorTaskResponse;
import com.dmantz.lms.service.InstructorDashboardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/instructor")
public class InstructorDashboardController {

	private static final Logger logger = LogManager.getLogger(InstructorDashboardController.class);

	private final InstructorDashboardService instructorDashboardService;

	public InstructorDashboardController(InstructorDashboardService instructorDashboardService) {
		this.instructorDashboardService = instructorDashboardService;
	}

	@PostMapping("/tasks")
	public ResponseEntity<InstructorTaskResponse> createTask(@Valid @RequestBody InstructorTaskRequest request) {

		logger.info("Received instructor create-task request for assignedBy: {} batchId: {} courseId: {}",
				request.getAssignedBy(), request.getBatchId(), request.getCourseId());

		InstructorTaskResponse response = instructorDashboardService.createTask(request);

		logger.info("Instructor task assigned to {} students in batch {}", response.getAssignedStudentCount(),
				response.getBatchId());

		return ResponseEntity.ok(response);
	}

}