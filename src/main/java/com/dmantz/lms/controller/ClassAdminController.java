package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.ScheduleFilter;
import com.dmantz.lms.service.ClassAdminService;
import com.dmantz.lms.service.StudentDashboardService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class ClassAdminController {

	private static final Logger logger = LogManager.getLogger(ClassAdminController.class);

	private final ClassAdminService classAdminService;
	private final StudentDashboardService dashboardService;

	public ClassAdminController(ClassAdminService classAdminService, StudentDashboardService dashboardService) {
		this.classAdminService = classAdminService;
		this.dashboardService = dashboardService;
	}

	// Add class
	@PostMapping("/courseschedule/{courseId}/classes")
	public ResponseEntity<ClassResponse> addClass(@PathVariable String courseId,
			@RequestBody CreateClassRequest request) {

		logger.info("POST /courses/{}/classes - Adding class for courseId: {}", courseId, courseId);

		ClassResponse response = classAdminService.addClass(courseId, request);

		logger.info("Class added successfully for courseId: {}", courseId);
		return ResponseEntity.ok(response);
	}

	// Modify class
	@PutMapping("/modify/courseschedule/classes/{batchId}")
	public ResponseEntity<ClassResponse> modifyClass(@PathVariable Long batchId,
			@RequestBody UpdateClassRequest request) {

		logger.info("PUT /courses/classes/{} - Modifying class", batchId);

		ClassResponse response = classAdminService.modifyClass(batchId, request);

		logger.info("Class modified successfully with batchId: {}", batchId);
		return ResponseEntity.ok(response);
	}

	// cancel class
	@PatchMapping("/courseschedule/classes/{batchId}/cancel")
	public ResponseEntity<ClassResponse> cancelClass(@PathVariable Long batchId) {

		logger.info("PATCH /courses/classes/{}/cancel - Cancelling class", batchId);

		ClassResponse response = classAdminService.cancelClass(batchId);

		logger.info("Class cancelled successfully with batchId: {}", batchId);
		return ResponseEntity.ok(response);
	}

	// Add schedule
	@PostMapping("/addschedule-to-class")
	public ResponseEntity<ClassScheduleResponse> addScheduleToClass(@Valid @RequestBody AddScheduleRequest request) {

		logger.info("POST /addschedule-to-class - Adding schedule to classId: {} with staffId: {}",
				request.getBatchId(), request.getStaffId());

		ClassScheduleResponse response = classAdminService.addScheduleToClass(request);

		logger.info("Schedule added successfully to classId: {}", request.getBatchId());
		return ResponseEntity.ok(response);
	}

	// Modify schedule
	@PutMapping("/courseschedule/{scheduleId}")
	public ResponseEntity<ClassScheduleResponse> modifySchedule(@PathVariable Long scheduleId,
			@RequestBody AddScheduleRequest request) {

		logger.info("PUT /schedules/{} - Mosdifying schedule", scheduleId);

		ClassScheduleResponse response = classAdminService.modifySchedule(scheduleId, request);

		logger.info("Schedule modified successfully with id: {}", scheduleId);
		return ResponseEntity.ok(response);
	}

	// Cancel schedule
	@PatchMapping("/schedules/{scheduleId}/cancel")
	public ResponseEntity<ClassScheduleResponse> cancelSchedule(@PathVariable Long scheduleId) {

		logger.info("PATCH /schedules/{}/cancel - Cancelling schedule", scheduleId);

		ClassScheduleResponse response = classAdminService.cancelSchedule(scheduleId);

		logger.info("Schedule cancelled successfully with id: {}", scheduleId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/student-details/{studentId}")
	public ResponseEntity<ClassAdminStudentDetailsResponse> getStudentDetails(@PathVariable String studentId) {
		logger.info("GET /student-details/{} - Fetching student details", studentId);

		ClassAdminStudentDetailsResponse response = classAdminService.viewStudentDetails(studentId);

		logger.debug("Student details fetched successfully for studentId: {}", studentId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/view-students")
	public ResponseEntity<List<ClassAdminStudentDetailsResponse>> viewStudents() {
		logger.info("GET /view-students - Fetching all students");

		List<ClassAdminStudentDetailsResponse> response = classAdminService.viewStudents();

		logger.debug("Returning details for {} student(s)", response.size());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/instructor/{staffId}")
	public ResponseEntity<List<InstructorScheduleResponse>> getSchedulesByInstructor(
	        @PathVariable String staffId,
	        @RequestParam(
	                required = false,
	                defaultValue = "ALL"
	        ) ScheduleFilter filter) {

	    List<InstructorScheduleResponse> schedules =
	    		classAdminService.getSchedulesByStaffId(
	                    staffId,
	                    filter
	            );

	    return ResponseEntity.ok(schedules);
	}

	@GetMapping("/staff/{staffId}/dailySchedules")
	public List<ClassScheduleResponse> getStaffDailySchedule(@PathVariable String staffId,
			@RequestParam LocalDate date) {

		logger.info("GET /staff/{}/dailySchedules - Fetching daily schedule for staffId: {} on date: {}", staffId,
				staffId, date);

		List<ClassScheduleResponse> schedules = classAdminService.getStaffDailySchedule(staffId, date);

		logger.debug("Returning {} schedule(s) for staffId: {} on date: {}", schedules.size(), staffId, date);
		return schedules;
	}

	@PostMapping("/classes/{batchId}/topics")
	public ResponseEntity<String> addTopicsToClass(@PathVariable Long batchId,
			@RequestBody AddClassTopicRequest request) {

		logger.info("POST /classes/{}/topics - Adding {} topic(s) to batchId: {}", batchId, request.getTopics().size(),
				batchId);

		classAdminService.addTopicsToClass(batchId, request);

		logger.info("Topics added successfully to batchId: {}", batchId);
		return ResponseEntity.ok("Topics added successfully");
	}

	@DeleteMapping("/classes/{batchId}/topics")
	public ResponseEntity<String> removeTopicsFromClass(@PathVariable Long batchId,
			@RequestBody RemoveClassTopicRequest removeClassTopicRequest) {

		logger.info("DELETE /classes/{}/topics - Removing {} topic(s) from batchId: {}", batchId,
				removeClassTopicRequest.getTopicIds().size(), batchId);

		classAdminService.removeTopicsFromClass(batchId, removeClassTopicRequest);

		logger.info("Topics removed successfully from batchId: {}", batchId);
		return ResponseEntity.ok("Topics removed successfully");
	}

	@GetMapping("/classes/{batchId}/topics")
	public ResponseEntity<List<ClassTopicResponse>> getTopicsByBatchId(@PathVariable Long batchId) {

		logger.info("GET /classes/{}/topics - Fetching topics for batchId: {}", batchId, batchId);

		List<ClassTopicResponse> topics = classAdminService.getTopicsByBatchId(batchId);

		logger.debug("Returning {} topic(s) for batchId: {}", topics.size(), batchId);
		return ResponseEntity.ok(topics);
	}

	@GetMapping("/courseschedule/{courseId}/classes")
	public ResponseEntity<List<ClassResponse>> getClassesByCourse(@PathVariable String courseId) {

		logger.info("GET /courses/{}/classes - Fetching batches", courseId);
		List<ClassResponse> response = classAdminService.getClassesByCourse(courseId);
		logger.info("Returning {} batch(es) for courseId: {}", response.size(), courseId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/courseschedule/{batchId}/schedules")
	public ResponseEntity<List<ClassScheduleResponse>> getSchedulesByBatch(@PathVariable Long batchId) {

		logger.info("GET /classes/{}/schedules - Fetching schedules", batchId);
		List<ClassScheduleResponse> response = classAdminService.getSchedulesByBatch(batchId);
		logger.info("Returning {} schedule(s) for batchId: {}", response.size(), batchId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/schedules")
	public ResponseEntity<List<ClassScheduleResponse>> getAllSchedules() {

	    logger.info("GET /schedules - Fetching all schedules");

	    List<ClassScheduleResponse> response =
	            classAdminService.getAllSchedules();

	    logger.info("Returning {} schedule(s)", response.size());

	    return ResponseEntity.ok(response);
	}
	@PostMapping("/{scheduleId}/assign-instructor")
	public ResponseEntity<String> assignInstructor(
	        @PathVariable Long scheduleId,
	        @Valid @RequestBody AssignInstructorRequest request) throws BadRequestException {

		classAdminService.assignInstructor(scheduleId, request);

	    return ResponseEntity.ok("Instructor assigned successfully");
	}
	
	@GetMapping("/courseschedule/classes/{batchId}/instructors")
	public ResponseEntity<List<BatchInstructorResponse>> getInstructorsByBatchId(@PathVariable Long batchId) {

	    logger.info("GET /courseschedule/classes/{}/instructors - Fetching instructors", batchId);

	    List<BatchInstructorResponse> response = classAdminService.getInstructorsByBatchId(batchId);

	    logger.debug("Returning {} instructor(s) for batchId: {}", response.size(), batchId);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/schedules/{scheduleId}")
	public ResponseEntity<ClassScheduleResponse> getScheduleById(@PathVariable Long scheduleId) {

	    logger.info("GET /schedules/{} - Fetching schedule", scheduleId);

	    ClassScheduleResponse response = classAdminService.getScheduleById(scheduleId);

	    logger.debug("Returning schedule details for id: {}", scheduleId);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/courseschedule/classes/{batchId}")
	public ResponseEntity<ClassResponse> getBatchById(@PathVariable Long batchId) {

	    logger.info("GET /courseschedule/classes/{} - Fetching batch", batchId);

	    ClassResponse response = classAdminService.getBatchById(batchId);

	    logger.debug("Returning batch details for batchId: {}", batchId);
	    return ResponseEntity.ok(response);
	}
	
	// Add extra instructors (appends to existing set)
	@PostMapping("/courseschedule/classes/{batchId}/instructors")
	public ResponseEntity<List<BatchInstructorResponse>> addInstructorsToBatch(
	        @PathVariable Long batchId,
	        @RequestBody BatchInstructorRequest request) {

	    logger.info("POST /courseschedule/classes/{}/instructors - Adding instructors", batchId);

	    List<BatchInstructorResponse> response = classAdminService.addInstructorsToBatch(batchId, request);

	    logger.debug("Returning {} instructor(s) for batchId: {}", response.size(), batchId);
	    return ResponseEntity.ok(response);
	}

	// Replace instructors entirely
	@PutMapping("/courseschedule/classes/{batchId}/instructors")
	public ResponseEntity<List<BatchInstructorResponse>> updateInstructorsForBatch(
	        @PathVariable Long batchId,
	        @RequestBody BatchInstructorRequest request) {

	    logger.info("PUT /courseschedule/classes/{}/instructors - Updating instructors", batchId);

	    List<BatchInstructorResponse> response = classAdminService.updateInstructorsForBatch(batchId, request);

	    logger.debug("Returning {} instructor(s) for batchId: {}", response.size(), batchId);
	    return ResponseEntity.ok(response);
	}
}
