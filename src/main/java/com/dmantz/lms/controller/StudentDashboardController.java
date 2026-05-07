package com.dmantz.lms.controller;

import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.CourseStatus;
import com.dmantz.lms.service.StudentDashboardService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-dashboard")
public class StudentDashboardController {

    private static final Logger logger =
            LogManager.getLogger(StudentDashboardController.class);

    private final StudentDashboardService dashboardService;

    public StudentDashboardController(StudentDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // ================= WEEKLY SCHEDULE =================

    @GetMapping("/weekly-schedule/{studentId}")
    public ResponseEntity<WeeklyScheduleResponse> getWeeklySchedule(
            @PathVariable String studentId) {

        logger.info(
                "Received request to fetch weekly schedule for studentId: {}",
                studentId
        );

        WeeklyScheduleResponse response =
                dashboardService.getWeeklySchedule(studentId);

        logger.info(
                "Weekly schedule fetched successfully for studentId: {}",
                studentId
        );

        return ResponseEntity.ok(response);
    }

    // ================= MY COURSES =================

    @GetMapping("/my-coursesprogress")
    public ResponseEntity<StudentMyCoursesResponse> getMyCourses(
            @RequestParam String studentId,
            @RequestParam(required = false) CourseStatus status) {

        logger.info(
                "Received request to fetch courses for studentId: {} with status: {}",
                studentId,
                status
        );

        StudentMyCoursesResponse response =
                dashboardService.getMyCourses(studentId, status);

        logger.info(
                "Courses fetched successfully for studentId: {}",
                studentId
        );

        return ResponseEntity.ok(response);
    }

    // ================= TOPIC PROGRESS =================

    @GetMapping("/{courseId}/topics-progress")
    public ResponseEntity<List<TopicProgressResponse>> getTopicProgress(
            @PathVariable String courseId,
            @RequestParam String studentId) {

        logger.info(
                "Received request to fetch topic progress for courseId: {} and studentId: {}",
                courseId,
                studentId
        );

        List<TopicProgressResponse> response =
                dashboardService.getTopicProgress(courseId, studentId);

        logger.info(
                "Topic progress fetched successfully for courseId: {} and studentId: {}",
                courseId,
                studentId
        );

        return ResponseEntity.ok(response);
    }

    // ================= CHAPTER PROGRESS =================

    @GetMapping("/{courseId}/chapters-progress")
    public ResponseEntity<List<ChapterProgressResponse>> getChapterProgress(
            @PathVariable String courseId,
            @RequestParam String studentId) {

        logger.info(
                "Received request to fetch chapter progress for courseId: {} and studentId: {}",
                courseId,
                studentId
        );

        List<ChapterProgressResponse> response =
                dashboardService.getChapterProgress(courseId, studentId);

        logger.info(
                "Chapter progress fetched successfully for courseId: {} and studentId: {}",
                courseId,
                studentId
        );

        return ResponseEntity.ok(response);
    }

    // ================= COURSE PROGRESS =================

    @GetMapping("/dashboard/course/{courseId}/progress")
    public ResponseEntity<CourseProgressSummaryResponse> getCourseProgress(
            @PathVariable String courseId,
            @RequestParam String studentId) {

        logger.info(
                "Received request to fetch course progress for courseId: {} and studentId: {}",
                courseId,
                studentId
        );

        CourseProgressSummaryResponse response =
                dashboardService.getCourseProgressSummary(courseId, studentId);

        logger.info(
                "Course progress fetched successfully for courseId: {} and studentId: {}",
                courseId,
                studentId
        );

        return ResponseEntity.ok(response);
    }

    // ================= CLASS INFO =================

    @GetMapping("/classes/{studentId}")
    public ResponseEntity<List<StudentClassResponse>> getMyClasses(
            @PathVariable String studentId) {

        logger.info(
                "Received request to fetch class info for studentId: {}",
                studentId
        );

        List<StudentClassResponse> response =
                dashboardService.getClassInfo(studentId);

        logger.info(
                "Class info fetched successfully for studentId: {}",
                studentId
        );

        return ResponseEntity.ok(response);
    }

    // ================= DASHBOARD SUMMARY =================

    @GetMapping("/summary/{studentId}")
    public ResponseEntity<StudentDashboardSummaryResponse> getDashboardSummary(
            @PathVariable String studentId) {

        logger.info(
                "Received request to fetch dashboard summary for studentId: {}",
                studentId
        );

        StudentDashboardSummaryResponse response =
                dashboardService.getDashboardSummary(studentId);

        logger.info(
                "Dashboard summary fetched successfully for studentId: {}",
                studentId
        );

        return ResponseEntity.ok(response);
    }
}