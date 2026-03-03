package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.response.*;
import com.dmantz.lms_b.entity.CourseStatus;

import java.util.List;

public interface StudentDashboardService {

	WeeklyScheduleResponse getWeeklySchedule(String studentId);

	StudentMyCoursesResponse getMyCourses(String studentId, CourseStatus status);

	List<TopicProgressResponse> getTopicProgress(Long courseId, Long studentId);

	List<ChapterProgressResponse> getChapterProgress(Long courseId, Long studentId);

	CourseProgressSummaryResponse getCourseProgressSummary(Long courseId, Long studentId);

	List<StudentClassResponse> getClassInfo(String studentId);

}
