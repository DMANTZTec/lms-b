package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.dto.request.PlanClassTopicsRequest;
import com.dmantz.lms.dto.response.*;

public interface InstructorDashboardService {

	InstructorTaskResponse createTask(InstructorTaskRequest request);

	InstructorBatchSummaryResponse getBatchSummary(String instructorId);

	InstructorClassStatsResponse getClassStats(String instructorId);

	InstructorStudentStatsResponse getStudentStats(String instructorId);

	List<StudentTaskSubmissionResponse> getTaskSubmissions(String staffId, String courseId);

	List<InstructorCourseResponse> getMyCourses(String instructorId);

	List<ClassTopicResponse> planClassTopics(Long scheduleId, PlanClassTopicsRequest request);

	List<ClassTopicResponse> getPlannedTopics(Long scheduleId, String staffId);

}