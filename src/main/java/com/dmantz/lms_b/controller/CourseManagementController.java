package com.dmantz.lms_b.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.service.CourseManagementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CourseManagementController {

	private final CourseManagementService courseManagementService;

	public CourseManagementController(CourseManagementService courseManagementService) {
		super();
		this.courseManagementService = courseManagementService;
	}

	@PostMapping("/subject/create")
	public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request,
			@RequestParam Long staffId) {

		SubjectResponse response = courseManagementService.createSubject(request, staffId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/subject/view-subjects")
	public ResponseEntity<List<SubjectResponse>> viewAllSubjects() {

		List<SubjectResponse> subjects = courseManagementService.viewAllSubjects();

		return ResponseEntity.ok(subjects);
	}

	@PutMapping("/subject/update/{subjectId}")
	public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long subjectId,
			@Valid @RequestBody SubjectRequest request, @RequestParam Long staffId) {
		return ResponseEntity.ok(courseManagementService.updateSubject(subjectId, request, staffId));
	}

	@DeleteMapping("/subject/delete/{subjectId}")
	public ResponseEntity<SubjectResponse> deleteSubject(@PathVariable Long subjectId, @RequestParam Long staffId) {
		return ResponseEntity.ok(courseManagementService.deleteSubject(subjectId, staffId));
	}
}
