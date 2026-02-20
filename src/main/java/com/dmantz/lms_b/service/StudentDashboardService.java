package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;

import java.util.List;

public interface StudentDashboardService {

//    List<ClassScheduleResponse> getMyClassScheduleThisWeek(String studentId);
//
//    StudentDashboardResponse getDashboard(String studentId);

    WeeklyScheduleResponse getWeeklySchedule(String studentId);

    ClassScheduleResponse addScheduleToClass(ClassScheduleRequest request);


}
