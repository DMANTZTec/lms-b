package com.dmantz.lms_b.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms_b.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms_b.dto.response.StudentTopicReferenceProgressResponse;

import com.dmantz.lms_b.service.StudentTopicReferenceProgressService;

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
