package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.StudentScheduleResponse;

import java.util.List;

public interface StudentDashboardService {

    List<StudentScheduleResponse> getMyClassScheduleThisWeek(String studentId);

    StudentDashboardResponse getDashboard(String studentId);


}
