package com.dmantz.lms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;

public interface CourseManagementService {
//	create a subject
	SubjectResponse createSubject(SubjectRequest requestDto, String staffID);

//  view all subject
	List<SubjectResponse> viewAllSubjects();

// update an existing subject
	SubjectResponse updateSubject(Long subjectId, SubjectRequest request, String staffId);

//	delete subject
	void deleteSubject(Long subjectId, String staffId);

//	create a course
	CourseResponse createCourse(CourseRequest requestDto, String staffId) throws Exception;

//	 view all courses
	List<CourseResponse> viewAllCourses();

//	 update an existing course
	CourseResponse updateCourse(Long courseId, CourseRequest request, String staffId) throws Exception;

//		delete subject
	void deleteCourse(Long courseId, String staffId);

//	view Courses by subjects 
	List<CourseResponse> viewCoursesBySubject(Long subjectId);

//	create a course
	ChapterResponse createChapter(String staffId, ChapterRequest request);

//	get chapter by Id
	ChapterResponse getChapterById(Long chapterId);

//    get all chapters
	List<ChapterResponse> getAllChapters();

//    update a chapter
	ChapterResponse updateChapter(Long chapterId, ChapterRequest request, String staffId);

//    delete a chapter
	void deleteChapter(Long chapterId, String staffId);

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

	TopicReferenceResponseDto addDocumentReference(Long topicId, DocumentReferenceRequestDto dto, MultipartFile file)
			throws Exception;

	TopicReferenceResponseDto addVideoReference(Long topicId, VideoReferenceRequestDto dto, MultipartFile file)
			throws Exception;

	TopicReferenceResponseDto addUrlReference(Long topicId, TopicUrlReferenceRequestDto dto) throws Exception;

	List<TopicReferenceDataDto> getDocumentsByTopicId(Long topicId);

	List<TopicReferenceDataDto> getVideosByTopicId(Long topicId);

	List<TopicReferenceDataDto> getUrlsByTopicId(Long topicId);

	String deleteDocument(Long referenceId);

	String deleteVideo(Long referenceId);

	String deleteUrl(Long referenceId);

	List<ProgramCourseResponse> addCoursesToProgram(ProgramCourseRequest request);

	void deleteProgramCourse(Long programCourseId);

	void deleteProgram(Long programId);

	ProgramResponse updateProgram(Long programId, ProgramRequest request);

	List<ProgramResponse> getAllPrograms();

	ProgramResponse getProgramById(Long id);

	ProgramResponse createProgram(ProgramRequest request);

	List<ChapterResponse> getChaptersByCourseStringId(String courseId);

	CourseDetailsResponse getCourseDetails(String courseId);

}
