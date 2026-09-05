package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.dto.response.InstructorBatchSummaryResponse;
import com.dmantz.lms.dto.response.InstructorClassStatsResponse;
import com.dmantz.lms.dto.response.InstructorStudentStatsResponse;
import com.dmantz.lms.dto.response.InstructorTaskResponse;

public interface InstructorDashboardService {

	InstructorTaskResponse createTask(InstructorTaskRequest request);

	InstructorBatchSummaryResponse getBatchSummary(String instructorId);

	InstructorClassStatsResponse getClassStats(String instructorId);

	InstructorStudentStatsResponse getStudentStats(String instructorId);

}