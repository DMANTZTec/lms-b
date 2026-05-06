package com.dmantz.lms.controller;

import java.util.List;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;
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

import com.dmantz.lms.service.CourseManagementService;

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
			@RequestParam String staffId) {

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
			@Valid @RequestBody SubjectRequest request, @RequestParam String staffId) {
		return ResponseEntity.ok(courseManagementService.updateSubject(subjectId, request, staffId));
	}

	@DeleteMapping("/subject/delete/{subjectId}")
	public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId, @RequestParam String staffId) {

		courseManagementService.deleteSubject(subjectId, staffId);

		return ResponseEntity.ok("Subject deleted successfully");
	}

	@PostMapping("/course/create")
	public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request,
			@RequestParam String staffId) {

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
			@Valid @RequestBody CourseRequest request, @RequestParam String staffId) {
		return ResponseEntity.ok(courseManagementService.updateCourse(courseId, request, staffId));
	}

	@DeleteMapping("/course/delete/{courseId}")
	public ResponseEntity<String> deleteCourse(@PathVariable Long courseId, @RequestParam String staffId) {
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
	public ResponseEntity<ChapterResponse> createChapter(@RequestParam String staffId,
			@RequestBody ChapterRequest request) {
		ChapterResponse response = courseManagementService.createChapter(staffId, request);
		return ResponseEntity.ok(response);
	}

	// ================= UPDATE =================
	@PutMapping("/update/{chapterId}")
	public ResponseEntity<ChapterResponse> updateChapter(@PathVariable Long chapterId, @RequestParam String staffId,
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
	public ResponseEntity<String> deleteChapter(@PathVariable Long chapterId, @RequestParam String staffId) {
		courseManagementService.deleteChapter(chapterId, staffId);
		return ResponseEntity.ok("Chapter deleted successfully");
	}

	// =============== CREATE TOPIC====================

	@PostMapping("/topics")
	public ResponseEntity<TopicResponseDto> createTopic(@Valid @RequestBody TopicRequestDto request) {

		TopicResponseDto response = courseManagementService.createTopic(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ================ Get All Topics by Chapter Id=====================

	@GetMapping("/topics")
	public ResponseEntity<List<TopicResponseDto>> getTopicsByChapterId(@RequestParam Long chapterId) {

		List<TopicResponseDto> topics = courseManagementService.getTopicsByChapterId(chapterId);

		return ResponseEntity.ok(topics);
	}

	// ====================== Get Topic by Id and Chapter Id
	// =========================

	@GetMapping("topics/{topicId}")
	public ResponseEntity<TopicResponseDto> getTopicByIdAndChapterId(@PathVariable Long topicId,
			@RequestParam Long chapterId) {

		TopicResponseDto response = courseManagementService.getTopicByIdAndChapterId(topicId, chapterId);

		return ResponseEntity.ok(response);
	}

	// =============== UPDATE TOPIC =============================

	@PutMapping("topics/{id}")
	public ResponseEntity<TopicResponseDto> updateTopic(@PathVariable Long id,
			@Valid @RequestBody TopicRequestDto requestDto) {

		TopicResponseDto response = courseManagementService.updateTopic(id, requestDto);
		return ResponseEntity.ok(response);
	}

	// ====================== DELETE TOPIC =================================

	@DeleteMapping("topics/{id}")
	public ResponseEntity<String> deleteTopic(@PathVariable Long id) {

		courseManagementService.deleteTopic(id);

		return ResponseEntity.ok("Topic deleted successfully");
	}

	// Move chapter to specific position
	@PutMapping("/{chapterId}/movechapter")
	public ResponseEntity<String> moveChapter(@PathVariable Long chapterId, @RequestParam int targetPosition) {

		courseManagementService.moveChapter(chapterId, targetPosition);

		return ResponseEntity.ok("Chapter moved successfully");
	}

//    move topic to specific position
	@PutMapping("/{topicId}/movetopic")
	public ResponseEntity<String> moveTopic(@PathVariable Long topicId, @RequestParam int targetPosition) {

		courseManagementService.moveTopic(topicId, targetPosition);
		return ResponseEntity.ok("Topic moved successfully");
	}

	// ========================== Add URL Reference =============================

	@PostMapping("topics/{topicId}/references/url")
	public ResponseEntity<TopicReferenceResponseDto> addUrl(@PathVariable Long topicId,
			@RequestBody TopicReferenceRequestDto dto) {

		return ResponseEntity.ok(courseManagementService.addUrlReference(topicId, dto));
	}

	// ========================= Add Video Reference =============================

	@PostMapping("topics/{topicId}/references/video")
	public ResponseEntity<TopicReferenceResponseDto> addVideo(@PathVariable Long topicId,
			@RequestBody TopicReferenceRequestDto dto) {

		return ResponseEntity.ok(courseManagementService.addVideoReference(topicId, dto));
	}

	// ========================= Add Document Reference

	@PostMapping("topics/{topicId}/references/document")
	public ResponseEntity<TopicReferenceResponseDto> addDocument(@PathVariable Long topicId,
			@RequestBody TopicReferenceRequestDto dto) {

		return ResponseEntity.ok(courseManagementService.addDocumentReference(topicId, dto));
	}

//	========================= Add course to program =============================
	@PostMapping("/add/course/program")
	public ResponseEntity<List<ProgramCourseResponse>> addCourses(@Valid @RequestBody ProgramCourseRequest request) {

		return ResponseEntity.ok(courseManagementService.addCoursesToProgram(request));
	}

//========================= delete course from program =============================
	@DeleteMapping("/remove/course/{id}")
	public ResponseEntity<String> removeCourse(@PathVariable Long id) {

		courseManagementService.deleteProgramCourse(id);
		return ResponseEntity.ok("Program-Course mapping removed successfully");
	}

	// ================= CREATE =================
	@PostMapping("/add/program")
	public ResponseEntity<ProgramResponse> createProgram(@RequestBody ProgramRequest request) {

		ProgramResponse response = courseManagementService.createProgram(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// ================= GET BY ID =================
	@GetMapping("/getById/program/{id}")
	public ResponseEntity<ProgramResponse> getProgramById(@PathVariable Long id) {

		ProgramResponse response = courseManagementService.getProgramById(id);
		return ResponseEntity.ok(response);
	}

	// ================= GET ALL =================
	@GetMapping("/getAll/program")
	public ResponseEntity<List<ProgramResponse>> getAllPrograms() {

	    List<ProgramResponse> response = courseManagementService.getAllPrograms();
	    return ResponseEntity.ok(response);
	}
	
	// ================= UPDATE =================
	@PutMapping("/update/program/{id}")
	public ResponseEntity<ProgramResponse> updateProgram(
	        @PathVariable Long id,
	        @RequestBody ProgramRequest request) {

	    ProgramResponse response = courseManagementService.updateProgram(id, request);
	    return ResponseEntity.ok(response);
	}

	// ================= DELETE =================
	@DeleteMapping("/delete/program/{id}")
	public ResponseEntity<String> deleteProgram(@PathVariable Long id) {

		courseManagementService.deleteProgram(id);
		return ResponseEntity.ok("Program deleted successfully");
	}
}
