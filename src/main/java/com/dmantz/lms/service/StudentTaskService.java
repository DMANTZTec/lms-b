package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.request.StudentTaskUpdateRequest;
import com.dmantz.lms.dto.response.HoursSpentResponse;
import com.dmantz.lms.dto.response.StudentTaskListResponse;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.entity.StudentNeedHelpRequest;

public interface StudentTaskService {

	StudentTaskResponse addTask(StudentTaskRequest request);

	StudentTaskResponse updateNeedHelp(StudentNeedHelpRequest request);

	HoursSpentResponse getHoursSpent(String studentId);

	StudentTaskResponse updateTask(StudentTaskUpdateRequest request);

	String deleteTask(String studentId, Long topicId);

	StudentTaskListResponse getStudentTasks(String studentId, String status);

	StudentTaskResponse markTaskCompleted(Long taskId, String studentId);

}