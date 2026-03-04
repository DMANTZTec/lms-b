package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.ChapterProgressResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.CourseProgressSummaryResponse;
import com.dmantz.lms_b.dto.response.StudentMyCoursesResponse;
import com.dmantz.lms_b.dto.response.TopicProgressResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms_b.entity.CourseStatus;
import com.dmantz.lms_b.service.StudentDashboardService;
import jakarta.validation.Valid;
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

	@GetMapping("/courses/{courseId}/topicsprogress")
	public ResponseEntity<List<TopicProgressResponse>> getTopicProgress(@PathVariable Long courseId,
			@RequestParam Long studentId) {

		return ResponseEntity.ok(dashboardService.getTopicProgress(courseId, studentId));
	}

	@GetMapping("/courses/{courseId}/chaptersprogress")
	public ResponseEntity<List<ChapterProgressResponse>> getChapterProgress(@PathVariable Long courseId,
			@RequestParam Long studentId) {

		return ResponseEntity.ok(dashboardService.getChapterProgress(courseId, studentId));
	}

	@GetMapping("/dashboard/course/{courseId}/progress")
	public ResponseEntity<CourseProgressSummaryResponse> getCourseProgress(@PathVariable Long courseId,
			@RequestParam Long studentId) {

		return ResponseEntity.ok(dashboardService.getCourseProgressSummary(courseId, studentId));
	}
}

//    @GetMapping("/{studentId}/dashboard")
//    public ResponseEntity<StudentDashboardResponse> getDashboard(@PathVariable String studentId) {
//        StudentDashboardResponse dashboard = dashboardService.getDashboard(studentId);
//        return ResponseEntity.ok(dashboard);
//    }
