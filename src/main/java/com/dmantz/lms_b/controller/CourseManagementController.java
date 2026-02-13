package com.dmantz.lms_b.controller;

import java.util.List;

import com.dmantz.lms_b.dto.request.TopicRequestDto;
import com.dmantz.lms_b.dto.response.TopicResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms_b.dto.request.ChapterRequest;
import com.dmantz.lms_b.dto.request.CourseRequest;
import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.ChapterResponse;
import com.dmantz.lms_b.dto.response.CourseResponse;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.service.CourseManagementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CourseManagementController {

	private final CourseManagementService courseManagementService;

	public CourseManagementController(CourseManagementService courseManagementService) {
		super();
		this.courseManagementService = courseManagementService;
	}

	@PostMapping("/subject/create")
	public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request,
			@RequestParam Long staffId) {

		SubjectResponse response = courseManagementService.createSubject(request, staffId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/subject/view-subjects")
	public ResponseEntity<List<SubjectResponse>> viewAllSubjects() {

		List<SubjectResponse> subjects = courseManagementService.viewAllSubjects();

		return ResponseEntity.ok(subjects);
	}

	@PutMapping("/subject/update/{subjectId}")
	public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long subjectId,
			@Valid @RequestBody SubjectRequest request, @RequestParam Long staffId) {
		return ResponseEntity.ok(courseManagementService.updateSubject(subjectId, request, staffId));
	}

	@DeleteMapping("/subject/delete/{subjectId}")
	public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId, @RequestParam Long staffId) {

		courseManagementService.deleteSubject(subjectId, staffId);

		return ResponseEntity.ok("Subject deleted successfully");
	}

	@PostMapping("/course/create")
	public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request,
			@RequestParam Long staffId) {

		CourseResponse response = courseManagementService.createCourse(request, staffId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/course/view-courses")
	public ResponseEntity<List<CourseResponse>> viewAllCourses() {

		List<CourseResponse> courses = courseManagementService.viewAllCourses();

		return ResponseEntity.ok(courses);
	}

	@PutMapping("/course/update/{courseId}")
	public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long courseId,
			@Valid @RequestBody CourseRequest request, @RequestParam Long staffId) {
		return ResponseEntity.ok(courseManagementService.updateCourse(courseId, request, staffId));
	}

	@DeleteMapping("/course/delete/{courseId}")
	public ResponseEntity<String> deleteCourse(@PathVariable Long courseId, @RequestParam Long staffId) {
		courseManagementService.deleteCourse(courseId, staffId);
		return ResponseEntity.ok("Course deleted successfully");
	}

//	get all courses by subject
	@GetMapping("/subjects/{subjectId}/view-courses")
	public ResponseEntity<List<CourseResponse>> viewCoursesBySubject(@PathVariable Long subjectId) {
		return ResponseEntity.ok(courseManagementService.viewCoursesBySubject(subjectId));
	}

	// ================= CREATE =================
	@PostMapping("/chapter/create")
	public ResponseEntity<ChapterResponse> createChapter(@RequestParam Long staffId,
			@RequestBody ChapterRequest request) {
		ChapterResponse response = courseManagementService.createChapter(staffId, request);
		return ResponseEntity.ok(response);
	}

	// ================= UPDATE =================
	@PutMapping("/update/{chapterId}")
	public ResponseEntity<ChapterResponse> updateChapter(@PathVariable Long chapterId, @RequestParam Long staffId,
			@RequestBody ChapterRequest request) {
		ChapterResponse response = courseManagementService.updateChapter(chapterId, request, staffId);
		return ResponseEntity.ok(response);
	}

	// ================= GET BY ID =================
	@GetMapping("/get/{chapterId}")
	public ResponseEntity<ChapterResponse> getChapterById(@PathVariable Long chapterId

	) {
		ChapterResponse response = courseManagementService.getChapterById(chapterId);
		return ResponseEntity.ok(response);
	}

	// ================= GET ALL CHAPTERS =================

	@GetMapping("/chapters/getAll")
	public ResponseEntity<List<ChapterResponse>> getChaptersByCourse(

	) {
		List<ChapterResponse> response = courseManagementService.getAllChapters();
		return ResponseEntity.ok(response);
	}

	// ================= DELETE =================
	@DeleteMapping("/delete/{chapterId}")
	public ResponseEntity<String> deleteChapter(@PathVariable Long chapterId, @RequestParam Long staffId) {
		courseManagementService.deleteChapter(chapterId, staffId);
		return ResponseEntity.ok("Chapter deleted successfully");
	}

	// =============== CREATE TOPIC====================

	@PostMapping("/topics")
	public ResponseEntity<TopicResponseDto> createTopic(
			@Valid @RequestBody TopicRequestDto request) {

		TopicResponseDto response = courseManagementService.createTopic(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ================ Get All Topics by Chapter Id=====================

	@GetMapping("/topics")
	public ResponseEntity<List<TopicResponseDto>> getTopicsByChapterId(
			@RequestParam Long chapterId) {

		List<TopicResponseDto> topics =
				courseManagementService.getTopicsByChapterId(chapterId);

		return ResponseEntity.ok(topics);
	}

	//====================== Get Topic by Id and Chapter Id =========================

	@GetMapping("topics/{topicId}")
	public ResponseEntity<TopicResponseDto> getTopicByIdAndChapterId(
			@PathVariable Long topicId,
			@RequestParam Long chapterId) {

		TopicResponseDto response =
				courseManagementService.getTopicByIdAndChapterId(topicId, chapterId);

		return ResponseEntity.ok(response);
	}

	// =============== UPDATE TOPIC =============================

	@PutMapping("topics/{id}")
	public ResponseEntity<TopicResponseDto> updateTopic(
			@PathVariable Long id,
			@Valid @RequestBody TopicRequestDto requestDto) {

		TopicResponseDto response = courseManagementService.updateTopic(id, requestDto);
		return ResponseEntity.ok(response);
	}

	// ====================== DELETE TOPIC =================================

	@DeleteMapping("topics/{id}")
	public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {

		courseManagementService.deleteTopic(id);

		return ResponseEntity.noContent().build();
	}

}
