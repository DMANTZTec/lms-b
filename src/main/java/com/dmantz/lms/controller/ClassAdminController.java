package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.ClassScheduleRequest;
import com.dmantz.lms.dto.request.CreateClassRequest;
import com.dmantz.lms.dto.request.UpdateClassRequest;
import com.dmantz.lms.dto.response.ClassAdminStudentDetailsResponse;
import com.dmantz.lms.dto.response.ClassResponse;
import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.service.ClassAdminService;
import com.dmantz.lms.service.StudentDashboardService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class ClassAdminController {

    private final ClassAdminService classAdminService;
    private final StudentDashboardService dashboardService;

    public ClassAdminController(ClassAdminService classAdminService, StudentDashboardService dashboardService) {
        this.classAdminService = classAdminService;
        this.dashboardService = dashboardService;
    }

    // Add class
    @PostMapping("/courses/{courseId}/classes")
    public ResponseEntity<ClassResponse> addClass(@PathVariable String courseId,
            @RequestBody CreateClassRequest request) {

        return ResponseEntity.ok(classAdminService.addClass(courseId, request));
    }

    // Modify class
    @PutMapping("courses/classes/{batchId}")
    public ResponseEntity<ClassResponse> modifyClass(@PathVariable Long batchId,
            @RequestBody UpdateClassRequest request) {

        return ResponseEntity.ok(classAdminService.modifyClass(batchId, request));
    }

    // cancel class
    @PatchMapping("courses/classes/{batchId}/cancel")
    public ResponseEntity<ClassResponse> cancelClass(@PathVariable Long batchId) {

        return ResponseEntity.ok(classAdminService.cancelClass(batchId));
    }

    // Add schedule
    @PostMapping("/addschedule-to-class")
    public ResponseEntity<ClassScheduleResponse> addScheduleToClass(
            @Valid @RequestBody ClassScheduleRequest request) {

        return ResponseEntity.ok(classAdminService.addScheduleToClass(request));
    }

    // Modify schedule
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ClassScheduleResponse> modifySchedule(@PathVariable Long scheduleId,
            @RequestBody ClassScheduleRequest request) {

        return ResponseEntity.ok(
                classAdminService.modifySchedule(scheduleId, request)
        );
    }

    //  Cancel schedule
    @PatchMapping("/schedules/{scheduleId}/cancel")
    public ResponseEntity<ClassScheduleResponse> cancelSchedule(
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(
                classAdminService.cancelSchedule(scheduleId)
        );
    }
    
    
    @GetMapping("/student-details/{studentId}")
    public ResponseEntity<ClassAdminStudentDetailsResponse> getStudentDetails(
            @PathVariable String studentId) {
        return ResponseEntity.ok(classAdminService.viewStudentDetails(studentId));
    }
    
    
    @GetMapping("/view-students")
    public ResponseEntity<List<ClassAdminStudentDetailsResponse>> viewStudents() {
        return ResponseEntity.ok(classAdminService.viewStudents());
    }

    @GetMapping("/schedules/staff/{staffId}")
    public ResponseEntity<List<ClassScheduleResponse>> getSchedulesByStaff(@PathVariable String staffId) {

        List<ClassScheduleResponse> schedules = classAdminService.getSchedulesByStaffId(staffId);

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/staff/{staffId}/dailySchedules")
    public List<ClassScheduleResponse> getStaffDailySchedule(
            @PathVariable String staffId,
            @RequestParam LocalDate date) {

        return classAdminService.getStaffDailySchedule(staffId, date);
    }


}
