package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.request.UpdateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.service.ClassAdminService;
import com.dmantz.lms_b.service.StudentDashboardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
public class ClassAdminController {

    private final ClassAdminService classAdminService;
    private final StudentDashboardService dashboardService;

    public ClassAdminController(ClassAdminService classAdminService, StudentDashboardService dashboardService) {
        this.classAdminService = classAdminService;
        this.dashboardService = dashboardService;
    }

    @PostMapping("/{courseId}/classes")
    public ResponseEntity<ClassResponse> addClass(
            @PathVariable String courseId,
            @RequestBody CreateClassRequest request) {

        return ResponseEntity.ok(classAdminService.addClass(courseId, request));
    }

    @PutMapping("/classes/{batchId}")
    public ResponseEntity<ClassResponse> modifyClass(
            @PathVariable Long batchId,
            @RequestBody UpdateClassRequest request) {

        return ResponseEntity.ok(classAdminService.modifyClass(batchId, request));
    }


    @PatchMapping("/classes/{batchId}/cancel")
    public ResponseEntity<ClassResponse> cancelClass(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(classAdminService.cancelClass(batchId));
    }

    @PostMapping("/addschedule-to-class")
    public ResponseEntity<ClassScheduleResponse> addScheduleToClass(
            @Valid @RequestBody ClassScheduleRequest request) {

        return ResponseEntity.ok(dashboardService.addScheduleToClass(request));
    }

}
