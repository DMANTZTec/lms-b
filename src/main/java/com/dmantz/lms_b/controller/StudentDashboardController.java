package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.StudentScheduleResponse;
import com.dmantz.lms_b.service.StudentDashboardService;
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

    @GetMapping("/my-schedule/this-week")
    public ResponseEntity<List<StudentScheduleResponse>> myScheduleThisWeek(
            @RequestParam String studentId) {

        return ResponseEntity.ok(
                dashboardService.getMyClassScheduleThisWeek(studentId));
    }

    @GetMapping("/{studentId}/dashboard")
    public ResponseEntity<StudentDashboardResponse> getDashboard(@PathVariable String studentId) {
        StudentDashboardResponse dashboard = dashboardService.getDashboard(studentId);
        return ResponseEntity.ok(dashboard);
    }
}
