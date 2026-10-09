package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.MarkAttendanceRequest;
import com.dmantz.lms.dto.response.MarkAttendanceResponse;
import com.dmantz.lms.dto.response.ScheduleAttendanceResponse;
import com.dmantz.lms.entity.SessionStatus;

public interface AttendanceService {

    SessionStatus getSessionStatus(Long scheduleId);

    int getPresentCount(Long scheduleId);

    double getAttendanceRate(Long scheduleId);

    ScheduleAttendanceResponse getScheduleAttendance(Long scheduleId);

    MarkAttendanceResponse markAttendance(Long scheduleId, MarkAttendanceRequest request);
}