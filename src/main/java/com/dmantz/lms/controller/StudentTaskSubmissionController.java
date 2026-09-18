package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.StudentTaskSubmissionRequest;
import com.dmantz.lms.dto.response.StudentTaskSubmissionResponse;
import com.dmantz.lms.entity.GitDetail;
import com.dmantz.lms.service.StudentTaskSubmissionService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/student-task-submission")
public class StudentTaskSubmissionController {

	private static final Logger logger = LogManager.getLogger(StudentTaskSubmissionController.class);

	private final StudentTaskSubmissionService submissionService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public StudentTaskSubmissionController(StudentTaskSubmissionService submissionService) {
		this.submissionService = submissionService;
	}

	// ================= SUBMIT TASK FOR REVIEW =================
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StudentTaskSubmissionResponse> submitTask(
			@Valid @ModelAttribute StudentTaskSubmissionRequest request) throws Exception {

		logger.info("Received submit task request for studentId: {} taskId: {}", request.getStudentId(),
				request.getStudentTaskId());

		StudentTaskSubmissionResponse response = submissionService.submitTask(request);

		return ResponseEntity.ok(response);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(List.class, "git", new PropertyEditorSupport() {

			@Override
			public void setAsText(String text) {

				if (text == null || text.isBlank()) {
					setValue(Collections.emptyList());
					return;
				}

				String trimmed = text.trim();

				String jsonArrayText = trimmed.startsWith("[") ? trimmed : "[" + trimmed + "]";

				try {
					List<GitDetail> gitDetails = objectMapper.readValue(jsonArrayText,
							new TypeReference<List<GitDetail>>() {
							});
					setValue(gitDetails);
				} catch (Exception e) {
					throw new IllegalArgumentException("Invalid 'git' JSON format: " + text, e);
				}
			}
		});
	}

}