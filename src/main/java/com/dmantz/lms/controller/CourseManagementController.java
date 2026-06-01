package com.dmantz.lms.controller;

import java.util.List;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmantz.lms.service.CourseManagementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CourseManagementController {

	private static final Logger logger = LogManager.getLogger(CourseManagementController.class);

	private final CourseManagementService courseManagementService;

	public CourseManagementController(CourseManagementService courseManagementService) {
		super();
		this.courseManagementService = courseManagementService;
	}

	@PostMapping("/subject/create")
	public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request,
			@RequestParam String staffId) {

		logger.info("POST /subject/create - Creating subject with shortCode: {} by staffId: {}",
				request.getSubjectShortCd(), staffId);

		SubjectResponse response = courseManagementService.createSubject(request, staffId);

		logger.info("Subject created successfully with shortCode: {}", request.getSubjectShortCd());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/subject/view-subjects")
	public ResponseEntity<List<SubjectResponse>> viewAllSubjects() {
		logger.info("GET /subject/view-subjects - Fetching all subjects");

		List<SubjectResponse> subjects = courseManagementService.viewAllSubjects();

		logger.debug("Returning {} subjects", subjects.size());
		return ResponseEntity.ok(subjects);
	}

	@PutMapping("/subject/update/{subjectId}")
	public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long subjectId,
			@Valid @RequestBody SubjectRequest request, @RequestParam String staffId) {

		logger.info("PUT /subject/update/{} - Updating subject by staffId: {}", subjectId, staffId);

		SubjectResponse response = courseManagementService.updateSubject(subjectId, request, staffId);

		logger.info("Subject updated successfully with id: {}", subjectId);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/subject/delete/{subjectId}")
	public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId, @RequestParam String staffId) {

		logger.info("DELETE /subject/delete/{} - Deleting subject by staffId: {}", subjectId, staffId);

		courseManagementService.deleteSubject(subjectId, staffId);

		logger.info("Subject deleted successfully with id: {}", subjectId);
		return ResponseEntity.ok("Subject deleted successfully");
	}

	@PostMapping("/course/create")
	public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request,
			@RequestParam String staffId) {

		logger.info("POST /course/create - Creating course: {} by staffId: {}", request.getCourseTitle(), staffId);

		CourseResponse response = courseManagementService.createCourse(request, staffId);
		logger.info("Course created successfully with title: {}", request.getCourseTitle());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/course/view-courses")
	public ResponseEntity<List<CourseResponse>> viewAllCourses() {

		logger.info("GET /course/view-courses - Fetching all courses");

		List<CourseResponse> courses = courseManagementService.viewAllCourses();

		logger.debug("Returning {} courses", courses.size());
		return ResponseEntity.ok(courses);
	}

	@PutMapping("/course/update/{courseId}")
	public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long courseId,
			@Valid @RequestBody CourseRequest request, @RequestParam String staffId) {
		logger.info("PUT /course/update/{} - Updating course by staffId: {}", courseId, staffId);

		CourseResponse response = courseManagementService.updateCourse(courseId, request, staffId);

		logger.info("Course updated successfully with id: {}", courseId);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/course/delete/{courseId}")
	public ResponseEntity<String> deleteCourse(@PathVariable Long courseId, @RequestParam String staffId) {
		logger.info("DELETE /course/delete/{} - Deleting course by staffId: {}", courseId, staffId);

		courseManagementService.deleteCourse(courseId, staffId);

		logger.info("Course deleted successfully with id: {}", courseId);
		return ResponseEntity.ok("Course deleted successfully");
	}

//	get all courses by subject
	@GetMapping("/subjects/{subjectId}/view-courses")
	public ResponseEntity<List<CourseResponse>> viewCoursesBySubject(@PathVariable Long subjectId) {
		logger.info("GET /subjects/{}/view-courses - Fetching courses for subjectId: {}", subjectId, subjectId);

		List<CourseResponse> courses = courseManagementService.viewCoursesBySubject(subjectId);

		logger.debug("Returning {} courses for subjectId: {}", courses.size(), subjectId);
		return ResponseEntity.ok(courses);
	}

	// ================= CREATE =================
	@PostMapping("/chapter/create")
	public ResponseEntity<ChapterResponse> createChapter(@RequestParam String staffId,
			@RequestBody ChapterRequest request) {

		logger.info("POST /chapter/create - Creating chapter: {} for courseId: {} by staffId: {}",
				request.getChapterNm(), request.getCourseId(), staffId);

		ChapterResponse response = courseManagementService.createChapter(staffId, request);

		logger.info("Chapter created successfully: {}", request.getChapterNm());
		return ResponseEntity.ok(response);
	}

	// ================= UPDATE =================
	@PutMapping("/update/{chapterId}")
	public ResponseEntity<ChapterResponse> updateChapter(@PathVariable Long chapterId, @RequestParam String staffId,
			@RequestBody ChapterRequest request) {

		logger.info("PUT /update/{} - Updating chapter by staffId: {}", chapterId, staffId);

		ChapterResponse response = courseManagementService.updateChapter(chapterId, request, staffId);

		logger.info("Chapter updated successfully with id: {}", chapterId);
		return ResponseEntity.ok(response);
	}

	// ================= GET BY ID =================
	@GetMapping("/get/{chapterId}")
	public ResponseEntity<ChapterResponse> getChapterById(@PathVariable Long chapterId

	) {

		logger.info("GET /get/{} - Fetching chapter by id", chapterId);
		ChapterResponse response = courseManagementService.getChapterById(chapterId);

		logger.debug("Chapter fetched successfully with id: {}", chapterId);
		return ResponseEntity.ok(response);
	}

	// ================= GET ALL CHAPTERS =================

	@GetMapping("/chapters/getAll")
	public ResponseEntity<List<ChapterResponse>> getChaptersByCourse(

	) {

		logger.info("GET /chapters/getAll - Fetching all chapters");

		List<ChapterResponse> response = courseManagementService.getAllChapters();

		logger.debug("Returning {} chapters", response.size());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/course/{courseId}/chapters")
	public ResponseEntity<List<ChapterResponse>> getChaptersByCourseId(@PathVariable String courseId) {

		logger.info("GET /course/{}/chapters - Fetching chapters for courseId: {}", courseId, courseId);

		List<ChapterResponse> response = courseManagementService.getChaptersByCourseStringId(courseId);

		logger.debug("Returning {} chapters for courseId: {}", response.size(), courseId);
		return ResponseEntity.ok(response);
	}

	// ================= DELETE =================
	@DeleteMapping("/delete/{chapterId}")
	public ResponseEntity<String> deleteChapter(@PathVariable Long chapterId, @RequestParam String staffId) {

		logger.info("DELETE /delete/{} - Deleting chapter by staffId: {}", chapterId, staffId);
		courseManagementService.deleteChapter(chapterId, staffId);

		logger.info("Chapter deleted successfully with id: {}", chapterId);
		return ResponseEntity.ok("Chapter deleted successfully");
	}

	// =============== CREATE TOPIC====================

	@PostMapping("/topics")
	public ResponseEntity<TopicResponseDto> createTopic(@Valid @RequestBody TopicRequestDto request) {

		logger.info("POST /topics - Creating topic: {} in chapterId: {} by staffId: {}", request.getTopicName(),
				request.getChapterId(), request.getStaffId());

		TopicResponseDto response = courseManagementService.createTopic(request);

		logger.info("Topic created successfully: {}", request.getTopicName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ================ Get All Topics by Chapter Id=====================

	@GetMapping("/topics")
	public ResponseEntity<List<TopicResponseDto>> getTopicsByChapterId(@RequestParam Long chapterId) {

		logger.info("GET /topics - Fetching topics for chapterId: {}", chapterId);

		List<TopicResponseDto> topics = courseManagementService.getTopicsByChapterId(chapterId);

		logger.debug("Returning {} topics for chapterId: {}", topics.size(), chapterId);
		return ResponseEntity.ok(topics);
	}

	// ====================== Get Topic by Id and Chapter Id
	// =========================

	@GetMapping("topics/{topicId}")
	public ResponseEntity<TopicResponseDto> getTopicByIdAndChapterId(@PathVariable Long topicId,
			@RequestParam Long chapterId) {

		logger.info("GET /topics/{} - Fetching topic in chapterId: {}", topicId, chapterId);

		TopicResponseDto response = courseManagementService.getTopicByIdAndChapterId(topicId, chapterId);

		logger.debug("Topic fetched successfully with id: {} in chapterId: {}", topicId, chapterId);
		return ResponseEntity.ok(response);
	}

	// =============== UPDATE TOPIC =============================

	@PutMapping("topics/{id}")
	public ResponseEntity<TopicResponseDto> updateTopic(@PathVariable Long id,
			@Valid @RequestBody TopicRequestDto requestDto) {

		logger.info("PUT /topics/{} - Updating topic", id);
		TopicResponseDto response = courseManagementService.updateTopic(id, requestDto);

		logger.info("Topic updated successfully with id: {}", id);
		return ResponseEntity.ok(response);
	}

	// ====================== DELETE TOPIC =================================

	@DeleteMapping("topics/{id}")
	public ResponseEntity<String> deleteTopic(@PathVariable Long id) {

		logger.info("DELETE /topics/{} - Deleting topic", id);

		courseManagementService.deleteTopic(id);

		logger.info("Topic deleted successfully with id: {}", id);
		return ResponseEntity.ok("Topic deleted successfully");
	}

	// Move chapter to specific position
	@PutMapping("/{chapterId}/movechapter")
	public ResponseEntity<String> moveChapter(@PathVariable Long chapterId, @RequestParam int targetPosition) {

		logger.info("PUT /{}/movechapter - Moving chapter to position: {}", chapterId, targetPosition);
		courseManagementService.moveChapter(chapterId, targetPosition);

		logger.info("Chapter {} moved to position {} successfully", chapterId, targetPosition);
		return ResponseEntity.ok("Chapter moved successfully");
	}

//    move topic to specific position
	@PutMapping("/{topicId}/movetopic")
	public ResponseEntity<String> moveTopic(@PathVariable Long topicId, @RequestParam int targetPosition) {

		logger.info("PUT /{}/movetopic - Moving topic to position: {}", topicId, targetPosition);
		courseManagementService.moveTopic(topicId, targetPosition);

		logger.info("Topic {} moved to position {} successfully", topicId, targetPosition);
		return ResponseEntity.ok("Topic moved successfully");
	}

//============================= Add document reference to topic =============================
	@PostMapping(value = "topics/{topicId}/references/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<TopicReferenceResponseDto> addDocument(@PathVariable Long topicId,
			@RequestParam("documentName") String documentName, @RequestParam("refBy") String refBy,
			@RequestParam("refById") String refById, @RequestPart("file") MultipartFile file) throws Exception {

		logger.info("POST /topics/{}/references/document", topicId);

		DocumentReferenceRequestDto dto = new DocumentReferenceRequestDto();
		dto.setDocumentName(documentName);
		dto.setRefBy(refBy);
		dto.setRefById(refById);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(courseManagementService.addDocumentReference(topicId, dto, file));
	}
//============================= Add video reference to topic =============================
	@PostMapping(value = "topics/{topicId}/references/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<TopicReferenceResponseDto> addVideo(@PathVariable Long topicId,
			@RequestParam("videoTitle") String videoTitle, @RequestParam("refBy") String refBy,
			@RequestParam("refById") String refById, @RequestPart("file") MultipartFile file) throws Exception {

		logger.info("POST /topics/{}/references/video", topicId);

		VideoReferenceRequestDto dto = new VideoReferenceRequestDto();
		dto.setVideoTitle(videoTitle);
		dto.setRefBy(refBy);
		dto.setRefById(refById);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(courseManagementService.addVideoReference(topicId, dto, file));
	}
//============================= Get all document references for a topic =============================
	@GetMapping("/topics/{topicId}/references/documents")
	public ResponseEntity<List<TopicReferenceDataDto>> getDocuments(@PathVariable Long topicId) {

		logger.info("GET /topics/{}/references/documents", topicId);

		List<TopicReferenceDataDto> response = courseManagementService.getDocumentsByTopicId(topicId);

		logger.debug("Returning {} document(s) for topicId: {}", response.size(), topicId);

		return ResponseEntity.ok(response);
	}
//============================= Get all video references for a topic =============================
	@GetMapping("/topics/{topicId}/references/videos")
	public ResponseEntity<List<TopicReferenceDataDto>> getVideos(@PathVariable Long topicId) {

		logger.info("GET /topics/{}/references/videos", topicId);

		List<TopicReferenceDataDto> response = courseManagementService.getVideosByTopicId(topicId);

		logger.debug("Returning {} video(s) for topicId: {}", response.size(), topicId);

		return ResponseEntity.ok(response);
	}
//============================= delete document for topic reference=============================
	@DeleteMapping("/references/document/{referenceId}")
	public ResponseEntity<String> deleteDocument(@PathVariable Long referenceId) {

		logger.info("DELETE /references/document/{}", referenceId);

		String response = courseManagementService.deleteDocument(referenceId);

		logger.info("Document deleted successfully with id: {}", referenceId);

		return ResponseEntity.ok(response);
	}
//============================= delete video for topic reference=============================
	@DeleteMapping("/references/video/{referenceId}")
	public ResponseEntity<String> deleteVideo(@PathVariable Long referenceId) {

		logger.info("DELETE /references/video/{}", referenceId);

		String response = courseManagementService.deleteVideo(referenceId);

		logger.info("Video deleted successfully with id: {}", referenceId);

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/topics/{topicId}/references/url")
	public ResponseEntity<TopicReferenceResponseDto> addUrl(@PathVariable Long topicId,
			@RequestBody TopicUrlReferenceRequestDto dto) throws Exception {

		logger.info("POST /topics/{}/references/url - Adding URL reference", topicId);

		return ResponseEntity.status(HttpStatus.CREATED).body(courseManagementService.addUrlReference(topicId, dto));
	}
	
	//=============================== GET URL REFERENCES BY TOPIC ID =========================
	@GetMapping("/topics/{topicId}/references/url")
	public ResponseEntity<List<TopicReferenceDataDto>> getUrlsByTopicId(
	        @PathVariable Long topicId) throws Exception {

	    logger.info("GET /topics/{}/references/url - Fetching URL references", topicId);

	    return ResponseEntity.ok(courseManagementService.getUrlsByTopicId(topicId));
	}

//	========================= Add course to program =============================
	@PostMapping("/add/course/program")
	public ResponseEntity<List<ProgramCourseResponse>> addCourses(@Valid @RequestBody ProgramCourseRequest request) {

		logger.info("POST /add/course/program - Adding {} course(s) to programId: {}", request.getCourseIds().size(),
				request.getProgramId());

		List<ProgramCourseResponse> response = courseManagementService.addCoursesToProgram(request);

		logger.info("Successfully added {} course(s) to programId: {}", response.size(), request.getProgramId());
		return ResponseEntity.ok(response);
	}

//========================= delete course from program =============================
	@DeleteMapping("/remove/course/{id}")
	public ResponseEntity<String> removeCourse(@PathVariable Long id) {

		logger.info("DELETE /remove/course/{} - Removing program-course mapping", id);
		courseManagementService.deleteProgramCourse(id);
		logger.info("Program-Course mapping removed successfully with id: {}", id);
		return ResponseEntity.ok("Program-Course mapping removed successfully");
	}

	// ================= CREATE =================
	@PostMapping("/add/program")
	public ResponseEntity<ProgramResponse> createProgram(@RequestBody ProgramRequest request) {

		logger.info("POST /add/program - Creating program: {} for providerId: {}", request.getProgramTitle(),
				request.getProviderId());

		ProgramResponse response = courseManagementService.createProgram(request);

		logger.info("Program created successfully with title: {}", request.getProgramTitle());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// ================= GET BY ID =================
	@GetMapping("/getById/program/{id}")
	public ResponseEntity<ProgramResponse> getProgramById(@PathVariable Long id) {

		logger.info("GET /getById/program/{} - Fetching program by id", id);
		ProgramResponse response = courseManagementService.getProgramById(id);

		logger.debug("Program fetched successfully with id: {}", id);
		return ResponseEntity.ok(response);
	}

	// ================= GET ALL =================
	@GetMapping("/getAll/program")
	public ResponseEntity<List<ProgramResponse>> getAllPrograms() {

		logger.info("GET /getAll/program - Fetching all programs");

		List<ProgramResponse> response = courseManagementService.getAllPrograms();
		logger.debug("Returning {} programs", response.size());
		return ResponseEntity.ok(response);
	}

	// ================= UPDATE =================
	@PutMapping("/update/program/{id}")
	public ResponseEntity<ProgramResponse> updateProgram(@PathVariable Long id, @RequestBody ProgramRequest request) {

		logger.info("PUT /update/program/{} - Updating program with title: {}", id, request.getProgramTitle());

		ProgramResponse response = courseManagementService.updateProgram(id, request);

		logger.info("Program updated successfully with id: {}", id);
		return ResponseEntity.ok(response);
	}

	// ================= DELETE =================
	@DeleteMapping("/delete/program/{id}")
	public ResponseEntity<String> deleteProgram(@PathVariable Long id) {

		logger.info("DELETE /delete/program/{} - Deleting program", id);
		courseManagementService.deleteProgram(id);

		logger.info("Program deleted successfully with id: {}", id);
		return ResponseEntity.ok("Program deleted successfully");
	}

	// ====================== GET COMPLETE COURSE DETAILS ======================

	@GetMapping("/coursedetails/{courseId}")
	public ResponseEntity<CourseDetailsResponse> getCourseDetails(@PathVariable String courseId) {

		logger.info("GET /course/details/{} - Fetching complete course details", courseId);

		CourseDetailsResponse response = courseManagementService.getCourseDetails(courseId);

		logger.info("Course details fetched successfully for courseId: {}", courseId);

		return ResponseEntity.ok(response);
	}
}
