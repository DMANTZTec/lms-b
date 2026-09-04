package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.dto.response.InstructorTaskResponse;

public interface InstructorDashboardService {

	InstructorTaskResponse createTask(InstructorTaskRequest request);

}