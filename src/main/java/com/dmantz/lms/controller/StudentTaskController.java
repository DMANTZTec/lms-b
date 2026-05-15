package com.dmantz.lms.controller;

import com.dmantz.lms.entity.StudentNeedHelpRequest;
import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.response.HoursSpentResponse;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.service.StudentTaskService;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-task")
public class StudentTaskController {

	private static final Logger logger = LogManager.getLogger(StudentTaskController.class);

	private final StudentTaskService studentTaskService;

	public StudentTaskController(StudentTaskService studentTaskService) {
		this.studentTaskService = studentTaskService;
	}

	// ================= ADD STUDENT TASK =================
	@PostMapping("/addtask")
	public ResponseEntity<StudentTaskResponse> addStudentTask(@Valid @RequestBody StudentTaskRequest request) {

		logger.info("Received add task request for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		StudentTaskResponse response = studentTaskService.addTask(request);

		logger.info("Task added successfully for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		return ResponseEntity.ok(response);
	}

	// ================= UPDATE NEED HELP =================
	@PatchMapping("/need-help")
	public ResponseEntity<StudentTaskResponse> markNeedHelp(@Valid @RequestBody StudentNeedHelpRequest request) {

		logger.info("Received need-help update request for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		StudentTaskResponse response = studentTaskService.updateNeedHelp(request);

		logger.info("Need-help status updated successfully for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/hours-spent/{studentId}")
	public ResponseEntity<HoursSpentResponse> getHoursSpent(@PathVariable String studentId) {
		logger.info("Received request to get hours spent for studentId: {}", studentId);
		HoursSpentResponse response = studentTaskService.getHoursSpent(studentId);
		logger.info("Hours spent retrieved successfully for studentId: {}", studentId);
		return ResponseEntity.ok(response);
	}
}