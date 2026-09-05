package com.dmantz.lms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms.dto.request.AcknowledgeMentorRequest;
import com.dmantz.lms.dto.request.StudentTaskMentorRequest;
import com.dmantz.lms.dto.request.UpdateMentorMinutesRequest;
import com.dmantz.lms.dto.response.MentorPointsResponse;
import com.dmantz.lms.dto.response.StudentTaskMentorResponse;
import com.dmantz.lms.service.StudentTaskMentorService;

@RestController
@RequestMapping("/api/student-task-mentor")
public class StudentTaskMentorController {

	private static final Logger logger = LogManager.getLogger(StudentTaskMentorController.class);

	@Autowired
	private StudentTaskMentorService mentorService;

	@PostMapping
	public ResponseEntity<StudentTaskMentorResponse> createMentoringActivity(
			@RequestBody StudentTaskMentorRequest request) {

		logger.info("Received request to create mentoring activity");

		StudentTaskMentorResponse response = mentorService.createMentoringActivity(request);

		logger.info("Mentoring activity created successfully");

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StudentTaskMentorResponse> updateMentoringMinutes(@PathVariable("id") Long id,
			@RequestBody UpdateMentorMinutesRequest request) {

		logger.info("Received request to update mentoring minutes for id: {}", id);

		StudentTaskMentorResponse response = mentorService.updateMentoringMinutes(id, request);

		logger.info("Mentoring minutes updated successfully for id: {}", id);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/acknowledge")
	public ResponseEntity<StudentTaskMentorResponse> acknowledgeMentorHelp(@PathVariable("id") Long id,
			@RequestBody AcknowledgeMentorRequest request) {

		logger.info("Received request to acknowledge mentor help for id: {}", id);

		StudentTaskMentorResponse response = mentorService.acknowledgeMentorHelp(id);

		logger.info("Mentor help acknowledged successfully for id: {}", id);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/summary/{studentId}")
	public ResponseEntity<MentorPointsResponse> getMentorPointsSummary(@PathVariable String studentId) {
		
		logger.info("Received mentor points summary request for studentId: {}", studentId);
		
		MentorPointsResponse response = mentorService.getMentorPointsSummary(studentId);
		
		logger.info("Returning mentor points summary for studentId: {}", studentId);
		
		return ResponseEntity.ok(response);
	}
}