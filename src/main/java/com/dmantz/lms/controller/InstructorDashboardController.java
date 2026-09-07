package com.dmantz.lms.controller;

import java.util.List;

import com.dmantz.lms.dto.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
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

		logger.info("Received instructor create-task request for assignedBy: {} courseId: {} chapterId: {} topicId: {}",
				request.getAssignedBy(), request.getCourseId(), request.getChapterId(), request.getTopicId());

		InstructorTaskResponse response = instructorDashboardService.createTask(request);

		logger.info("Instructor task assigned to {} students in course {}", response.getAssignedStudentCount(),
				response.getCourseId());

		return ResponseEntity.ok(response);
	}


	@GetMapping("/batches")
	public ResponseEntity<InstructorBatchSummaryResponse> getBatchSummary(@RequestParam String instructorId) {

		logger.info("Received request for active/completed batch summary for instructorId: {}", instructorId);

		InstructorBatchSummaryResponse response = instructorDashboardService.getBatchSummary(instructorId);

		logger.info("Returning {} active and {} completed batches for instructorId: {}",
				response.getActiveBatchCount(), response.getCompletedBatchCount(), instructorId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/class-stats")
	public ResponseEntity<InstructorClassStatsResponse> getClassStats(@RequestParam String instructorId) {

		logger.info("Received request for classes-taken/scheduled/hours-spent stats for instructorId: {}",
				instructorId);

		InstructorClassStatsResponse response = instructorDashboardService.getClassStats(instructorId);

		logger.info("Returning classesTaken: {}, scheduled: {}, hoursSpent: {} for instructorId: {}",
				response.getClassesTaken(), response.getScheduled(), response.getHoursSpent(), instructorId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/student-stats")
	public ResponseEntity<InstructorStudentStatsResponse> getStudentStats(@RequestParam String instructorId) {

		logger.info("Received request for active/total student stats for instructorId: {}", instructorId);

		InstructorStudentStatsResponse response = instructorDashboardService.getStudentStats(instructorId);

		logger.info("Returning activeStudents: {}, totalStudents: {} for instructorId: {}",
				response.getActiveStudents(), response.getTotalStudents(), instructorId);

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/submissions")
	 public ResponseEntity<List<StudentTaskSubmissionResponse>> getTaskSubmissions(
			 @RequestParam String staffId) {

	        logger.info("Fetching task submissions for staffId: {}", staffId);

	        List<StudentTaskSubmissionResponse> response = instructorDashboardService.getTaskSubmissions(staffId);

	        return ResponseEntity.ok(response);
	    }

	@GetMapping("/courses")
	public ResponseEntity<List<InstructorCourseResponse>> getMyCourses(@RequestParam String instructorId) {

		logger.info("Received request for my-courses payload for instructorId: {}", instructorId);

		List<InstructorCourseResponse> response = instructorDashboardService.getMyCourses(instructorId);

		logger.info("Returning {} course(s) for instructorId: {}", response.size(), instructorId);

		return ResponseEntity.ok(response);
	}


}