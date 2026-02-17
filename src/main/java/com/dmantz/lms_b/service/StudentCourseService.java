package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms_b.dto.response.StudentCourseResponse;
import com.dmantz.lms_b.dto.response.StudentDashboardResponse;

import java.util.List;

public interface StudentCourseService {

    StudentCourseResponse enroll(StudentCourseEnrollRequest request);

    List<StudentCourseResponse> getStudentCourses(String studentId);


}
