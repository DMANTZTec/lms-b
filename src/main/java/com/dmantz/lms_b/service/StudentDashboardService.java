package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.StudentMyCoursesResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms_b.entity.CourseStatus;

import java.util.List;

public interface StudentDashboardService {

    WeeklyScheduleResponse getWeeklySchedule(String studentId);

    StudentMyCoursesResponse getMyCourses(String studentId,
            CourseStatus status);
}
