package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.StudentMyCoursesResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms_b.entity.CourseStatus;

import java.util.List;

public interface StudentDashboardService {

//    List<ClassScheduleResponse> getMyClassScheduleThisWeek(String studentId);
//
//    StudentDashboardResponse getDashboard(String studentId);

    WeeklyScheduleResponse getWeeklySchedule(String studentId);

    ClassScheduleResponse addScheduleToClass(ClassScheduleRequest request);


//    StudentMyCoursesResponse getMyCourses(
//            String studentId,
//            CourseStatus status);

//    StudentMyCoursesResponse getMyCourses(String studentId);

    StudentMyCoursesResponse getMyCourses(
            String studentId,
            CourseStatus status);
}
