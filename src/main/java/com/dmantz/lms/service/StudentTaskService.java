package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.request.StudentTaskUpdateRequest;
import com.dmantz.lms.dto.response.ChapterDropdownResponse;
import com.dmantz.lms.dto.response.CourseDropdownResponse;
import com.dmantz.lms.dto.response.HoursSpentResponse;
import com.dmantz.lms.dto.response.StudentTaskListResponse;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.dto.response.TopicDropdownResponse;
import com.dmantz.lms.entity.StudentNeedHelpRequest;

public interface StudentTaskService {

	StudentTaskResponse addTask(StudentTaskRequest request);

	List<CourseDropdownResponse> getEnrolledCourses(String studentId);

	List<ChapterDropdownResponse> getChaptersByCourse(String courseId);

	List<TopicDropdownResponse> getTopicsByChapter(Long chapterId);

	StudentTaskListResponse getTasksByStatus(String studentId, String statusFilter);
}