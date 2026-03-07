package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.response.*;
import com.dmantz.lms_b.entity.CourseStatus;

import java.util.List;

public interface StudentDashboardService {

	WeeklyScheduleResponse getWeeklySchedule(String studentId);

	StudentMyCoursesResponse getMyCourses(String studentId, CourseStatus status);

	List<TopicProgressResponse> getTopicProgress(String courseId, String studentId); // ← String

	List<ChapterProgressResponse> getChapterProgress(String courseId, String studentId); // ← String

	CourseProgressSummaryResponse getCourseProgressSummary(String courseId, String studentId); // ← String

	List<StudentClassResponse> getClassInfo(String studentId);

	StudentDashboardSummaryResponse getDashboardSummary(String studentId);
}