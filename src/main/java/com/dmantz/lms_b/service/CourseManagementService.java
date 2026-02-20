package com.dmantz.lms_b.service;

import java.util.List;

import com.dmantz.lms_b.dto.request.ChapterRequest;
import com.dmantz.lms_b.dto.request.CourseRequest;
import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.request.TopicRequestDto;
import com.dmantz.lms_b.dto.response.ChapterResponse;
import com.dmantz.lms_b.dto.response.CourseResponse;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.dto.response.TopicResponseDto;

public interface CourseManagementService {
//	create a subject
	SubjectResponse createSubject(SubjectRequest requestDto, Long staffID);

//  view all subject
	List<SubjectResponse> viewAllSubjects();

// update an existing subject
	SubjectResponse updateSubject(Long subjectId, SubjectRequest request, Long staffId);

//	delete subject
	void deleteSubject(Long subjectId, Long staffId);

//	create a course
	CourseResponse createCourse(CourseRequest requestDto, Long staffId);

//	 view all courses
	List<CourseResponse> viewAllCourses();

//	 update an existing course
	CourseResponse updateCourse(Long courseId, CourseRequest request, Long staffId);

//		delete subject
	void deleteCourse(Long courseId, Long staffId);

//	view Courses by subjects 
	List<CourseResponse> viewCoursesBySubject(Long subjectId);

//	create a course
	ChapterResponse createChapter(Long staffId, ChapterRequest request);

//	get chapter by Id
	ChapterResponse getChapterById(Long chapterId);

//    get all chapters
	List<ChapterResponse> getAllChapters();

//    update a chapter
	ChapterResponse updateChapter(Long chapterId, ChapterRequest request, Long staffId);

//    delete a chapter
	void deleteChapter(Long chapterId, Long staffId);

//	create a topic
	TopicResponseDto createTopic(TopicRequestDto request);

//	get all topics in a chapter
	List<TopicResponseDto> getTopicsByChapterId(Long chapterId);

//	get topic by id and chapterId
	TopicResponseDto getTopicByIdAndChapterId(Long topicId, Long chapterId);

//	update a topic
	TopicResponseDto updateTopic(Long id, TopicRequestDto requestDto);

//	delete a topic
	void deleteTopic(Long id);
	
	
//	move chapter
	void moveChapter(Long chapterId, int targetPosition);

//	move topic
	void moveTopic(Long topicId, int targetPosition);

}
