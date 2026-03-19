package com.dmantz.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms.dto.response.StudentTopicReferenceProgressResponse;

import com.dmantz.lms.service.StudentTopicReferenceProgressService;

@RestController
@RequestMapping("/api/progress")
public class StudentTopicReferenceProgressController {

	private final StudentTopicReferenceProgressService studentTopicReferenceProgressService;

	public StudentTopicReferenceProgressController(
			StudentTopicReferenceProgressService studentTopicReferenceProgressService) {
		super();
		this.studentTopicReferenceProgressService = studentTopicReferenceProgressService;
	}

	@PostMapping("/markascomplete")
	public ResponseEntity<StudentTopicReferenceProgressResponse> markReferenceCompleted(
			@RequestBody StudentTopicReferenceProgressRequest request) {

		return ResponseEntity.ok(studentTopicReferenceProgressService.markReferenceComplete(request));
	}
}
