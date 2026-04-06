package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.EnrollStudentRequest;
import com.dmantz.lms.dto.response.EnrollStudentResponse;

import java.util.List;

public interface ClassStudentService {

    List<EnrollStudentResponse> enrollStudents(EnrollStudentRequest request);

}
