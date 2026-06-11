package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.AssignInstructorToCourseRequest;
import com.dmantz.lms.dto.response.InstructorResponse;
import com.dmantz.lms.dto.response.StaffCourseResponse;
import com.dmantz.lms.service.StaffCourseService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-course")
public class StaffCourseController {

    private static final Logger logger =
            LogManager.getLogger(StaffCourseController.class);

    private final StaffCourseService staffCourseService;

    public StaffCourseController(StaffCourseService staffCourseService) {
        this.staffCourseService = staffCourseService;
    }

    // Assign instructors to course
    @PostMapping("/assign")
    public ResponseEntity<String> assignInstructorsToCourse(
            @RequestParam String courseId,
            @Valid @RequestBody AssignInstructorToCourseRequest request) {

        logger.info("POST /assign - Assigning {} instructor(s) to courseId: {}",
                request.getStaffIds().size(), courseId);

        staffCourseService.assignInstructorsToCourse(courseId, request);

        logger.info("Instructors assigned successfully to courseId: {}",
                courseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Instructors assigned successfully");
    }

    // Get instructors by course → UI chips
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<InstructorResponse>> getInstructorsByCourse(
            @PathVariable String courseId) {

        logger.info("GET /course/{} - Fetching instructors", courseId);

        List<InstructorResponse> response = staffCourseService
                .getInstructorsByCourse(courseId);

        logger.info("Returning {} instructor(s) for courseId: {}",
                response.size(), courseId);
        return ResponseEntity.ok(response);
    }

    // Remove instructor from course
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeInstructorFromCourse(
            @RequestParam String courseId,
            @RequestParam String staffId) {

        logger.info("DELETE /remove - Removing staffId: {} from courseId: {}",
                staffId, courseId);

        staffCourseService.removeInstructorFromCourse(courseId, staffId);

        logger.info("StaffId: {} removed from courseId: {}",
                staffId, courseId);
        return ResponseEntity.ok("Instructor removed successfully");
    }

    // Get all courses assigned to a staff
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffCourseResponse>> getCoursesByStaff(
            @PathVariable String staffId) {

        logger.info("GET /staff/{} - Fetching courses for staffId: {}",
                staffId, staffId);

        List<StaffCourseResponse> response = staffCourseService
                .getCoursesByStaff(staffId);

        logger.info("Returning {} course(s) for staffId: {}",
                response.size(), staffId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/instructors")
    public ResponseEntity<List<InstructorResponse>> getAllInstructors() {

        return ResponseEntity.ok(
        		staffCourseService.getAllInstructors());
    }
}