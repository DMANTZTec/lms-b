package com.dmantz.lms.controller;

import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.CourseStatus;
import com.dmantz.lms.service.StudentDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-dashboard")
public class StudentDashboardController {

	private final StudentDashboardService dashboardService;

	public StudentDashboardController(StudentDashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/weekly-schedule/{studentId}")
	public ResponseEntity<WeeklyScheduleResponse> getWeeklySchedule(@PathVariable String studentId) {

		return ResponseEntity.ok(dashboardService.getWeeklySchedule(studentId));
	}

	@GetMapping("/my-coursesprogress")
	public ResponseEntity<StudentMyCoursesResponse> getMyCourses(@RequestParam String studentId,
			@RequestParam(required = false) CourseStatus status) {

		return ResponseEntity.ok(dashboardService.getMyCourses(studentId, status));
	}

	@GetMapping("/{courseId}/topics-progress")
	public ResponseEntity<List<TopicProgressResponse>> getTopicProgress(@PathVariable String courseId,
			@RequestParam String studentId) {

		return ResponseEntity.ok(dashboardService.getTopicProgress(courseId, studentId));
	}

	@GetMapping("/{courseId}/chapters-progress")
	public ResponseEntity<List<ChapterProgressResponse>> getChapterProgress(@PathVariable String courseId,
			@RequestParam String studentId) {

		return ResponseEntity.ok(dashboardService.getChapterProgress(courseId, studentId));
	}

	@GetMapping("/dashboard/course/{courseId}/progress")
	public ResponseEntity<CourseProgressSummaryResponse> getCourseProgress(@PathVariable String courseId,
			@RequestParam String studentId) {

		return ResponseEntity.ok(dashboardService.getCourseProgressSummary(courseId, studentId));
	}

	@GetMapping("/classes/{studentId}")
	public ResponseEntity<List<StudentClassResponse>> getMyClasses(@PathVariable String studentId) {

		return ResponseEntity.ok(dashboardService.getClassInfo(studentId));
	}

	@GetMapping("/summary/{studentId}")
	public ResponseEntity<StudentDashboardSummaryResponse> getDashboardSummary(@PathVariable String studentId) {
		return ResponseEntity.ok(dashboardService.getDashboardSummary(studentId));
	}
}