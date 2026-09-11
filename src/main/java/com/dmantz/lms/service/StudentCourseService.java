package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms.dto.response.StudentCourseResponse;
import java.util.List;

public interface StudentCourseService {

	StudentCourseResponse enroll(StudentCourseEnrollRequest request);

	List<StudentCourseResponse> getStudentCourses(String studentId);

}
