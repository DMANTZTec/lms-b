package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.dto.response.*;

public interface InstructorDashboardService {

	InstructorTaskResponse createTask(InstructorTaskRequest request);

	InstructorBatchSummaryResponse getBatchSummary(String instructorId);

	InstructorClassStatsResponse getClassStats(String instructorId);

	InstructorStudentStatsResponse getStudentStats(String instructorId);

	List<StudentTaskSubmissionResponse> getTaskSubmissions(String staffId);

	List<InstructorCourseResponse> getMyCourses(String instructorId);

}