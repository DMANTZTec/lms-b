package com.dmantz.lms.controller;

import com.dmantz.lms.entity.StudentNeedHelpRequest;
import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.request.StudentTaskUpdateRequest;
import com.dmantz.lms.dto.response.ChapterDropdownResponse;
import com.dmantz.lms.dto.response.CourseDropdownResponse;
import com.dmantz.lms.dto.response.HoursSpentResponse;
import com.dmantz.lms.dto.response.StudentTaskListResponse;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.dto.response.TopicDropdownResponse;
import com.dmantz.lms.service.StudentTaskService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-task")
public class StudentTaskController {

	private static final Logger logger = LogManager.getLogger(StudentTaskController.class);

	private final StudentTaskService studentTaskService;

	public StudentTaskController(StudentTaskService studentTaskService) {
		this.studentTaskService = studentTaskService;
	}

	@PostMapping("/addtask")
	public ResponseEntity<StudentTaskResponse> addStudentTask(@Valid @RequestBody StudentTaskRequest request) {

		logger.info("Received add task request for authenticated student, topicId: {}", request.getTopicId());

		StudentTaskResponse response = studentTaskService.addTask(request);

		logger.info("Task added successfully with id: {}", response.getId());

		return ResponseEntity.ok(response);
	}

	// ================= DROPDOWN: ENROLLED COURSES =================
	@GetMapping("/dropdown/courses")
	public ResponseEntity<List<CourseDropdownResponse>> getEnrolledCourses(@RequestParam String studentId) {
		return ResponseEntity.ok(studentTaskService.getEnrolledCourses(studentId));
	}

	// ================= DROPDOWN: CHAPTERS BY COURSE =================
	@GetMapping("/dropdown/chapters")
	public ResponseEntity<List<ChapterDropdownResponse>> getChaptersByCourse(@RequestParam String courseId) {
		return ResponseEntity.ok(studentTaskService.getChaptersByCourse(courseId));
	}

	// ================= DROPDOWN: TOPICS BY CHAPTER =================
	@GetMapping("/dropdown/topics")
	public ResponseEntity<List<TopicDropdownResponse>> getTopicsByChapter(@RequestParam Long chapterId) {
		return ResponseEntity.ok(studentTaskService.getTopicsByChapter(chapterId));
	}

	// ================= GET TASKS BY STATUS (ACTIVE / COMPLETED) =================
	@GetMapping("/status")
	public ResponseEntity<StudentTaskListResponse> getTasksByStatus(@RequestParam String studentId,
			@Parameter(schema = @Schema(allowableValues = { "ACTIVE", "COMPLETED" })) @RequestParam String status) {

		logger.info("Received get tasks by status request for studentId: {} status: {}", studentId, status);

		StudentTaskListResponse response = studentTaskService.getTasksByStatus(studentId, status);

		return ResponseEntity.ok(response);
	}

//	// ================= UPDATE NEED HELP =================
//	@PatchMapping("/need-help")
//	public ResponseEntity<StudentTaskResponse> markNeedHelp(@Valid @RequestBody StudentNeedHelpRequest request) {
//
//		logger.info("Received need-help update request for studentId: {} and topicId: {}", request.getStudentId(),
//				request.getTopicId());
//
//		StudentTaskResponse response = studentTaskService.updateNeedHelp(request);
//
//		logger.info("Need-help status updated successfully for studentId: {} and topicId: {}", request.getStudentId(),
//				request.getTopicId());
//
//		return ResponseEntity.ok(response);
//	}
//
//	@GetMapping("/hours-spent/{studentId}")
//	public ResponseEntity<HoursSpentResponse> getHoursSpent(@PathVariable String studentId) {
//		logger.info("Received request to get hours spent for studentId: {}", studentId);
//		HoursSpentResponse response = studentTaskService.getHoursSpent(studentId);
//		logger.info("Hours spent retrieved successfully for studentId: {}", studentId);
//		return ResponseEntity.ok(response);
//	}
//	// ================= UPDATE TASK =================
//
//	@PutMapping("/updatetask")
//	public ResponseEntity<StudentTaskResponse> updateStudentTask(@Valid @RequestBody StudentTaskUpdateRequest request) {
//
//		logger.info("Received update task request for studentId: {} and topicId: {}", request.getStudentId(),
//				request.getTopicId());
//
//		StudentTaskResponse response = studentTaskService.updateTask(request);
//
//		return ResponseEntity.ok(response);
//	}
//
//	// ================= DELETE TASK =================
//
//	@DeleteMapping("/deletetask")
//	public ResponseEntity<String> deleteTask(@RequestParam String studentId, @RequestParam Long topicId) {
//
//		logger.info("Received delete task request for studentId: {} and topicId: {}", studentId, topicId);
//
//		String response = studentTaskService.deleteTask(studentId, topicId);
//
//		return ResponseEntity.ok(response);
//	}
//
//	// ================= GET STUDENT TASKS =================
//	@GetMapping
//	public ResponseEntity<StudentTaskListResponse> getStudentTasks(@RequestParam String studentId,
//			@Parameter(schema = @Schema(allowableValues = { "ACTIVE",
//					"COMPLETED" })) @RequestParam(required = false) String status) {
//
//		logger.info("Received get tasks request for studentId: {} with status: {}", studentId, status);
//
//		StudentTaskListResponse response = studentTaskService.getStudentTasks(studentId, status);
//
//		logger.info("Fetched {} tasks successfully for studentId: {}", response.getCount(), studentId);
//
//		return ResponseEntity.ok(response);
//	}
//
//	// ================= MARK TASK COMPLETED =================
//	@PatchMapping("/{taskId}/complete")
//	public ResponseEntity<StudentTaskResponse> markTaskCompleted(@PathVariable Long taskId,
//			@RequestParam String studentId) {
//
//		logger.info("PATCH /api/student-tasks/{}/complete - Marking task completed for studentId: {}", taskId,
//				studentId);
//
//		StudentTaskResponse response = studentTaskService.markTaskCompleted(taskId, studentId);
//
//		return ResponseEntity.ok(response);
//	}

}