package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.MarkAttendanceRequest;
import com.dmantz.lms.dto.response.MarkAttendanceResponse;
import com.dmantz.lms.dto.response.ScheduleAttendanceResponse;
import com.dmantz.lms.service.AttendanceService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor")
public class InstructorAttendanceController {

    private static final Logger logger = LogManager.getLogger(InstructorAttendanceController.class);

    private final AttendanceService attendanceService;

    public InstructorAttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/schedules/{scheduleId}/attendance")
    public ResponseEntity<ScheduleAttendanceResponse> getScheduleAttendance(@PathVariable Long scheduleId) {
        logger.info("GET /api/instructor/schedules/{}/attendance", scheduleId);
        ScheduleAttendanceResponse response = attendanceService.getScheduleAttendance(scheduleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/schedules/{scheduleId}/attendance/mark")
    public ResponseEntity<MarkAttendanceResponse> markAttendancePost(
            @PathVariable Long scheduleId,
            @Valid @RequestBody MarkAttendanceRequest request
    ) {
        logger.info("POST /api/instructor/schedules/{}/attendance/mark", scheduleId);
        MarkAttendanceResponse response = attendanceService.markAttendance(scheduleId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedules/{scheduleId}/attendance/mark")
    public ResponseEntity<MarkAttendanceResponse> markAttendancePut(
            @PathVariable Long scheduleId,
            @Valid @RequestBody MarkAttendanceRequest request
    ) {
        logger.info("PUT /api/instructor/schedules/{}/attendance/mark", scheduleId);
        MarkAttendanceResponse response = attendanceService.markAttendance(scheduleId, request);
        return ResponseEntity.ok(response);
    }
}
