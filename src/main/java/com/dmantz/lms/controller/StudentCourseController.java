package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms.dto.response.StudentCourseResponse;
import com.dmantz.lms.service.StudentCourseService;
import com.dmantz.lms.service.impl.StudentCourseServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-course")
public class StudentCourseController {
	
	private static final Logger log = LogManager.getLogger(StudentCourseController.class);

    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    // ================= ENROLL COURSE =================
    @PostMapping("/enroll")
    public ResponseEntity<StudentCourseResponse> enroll(
            @RequestBody StudentCourseEnrollRequest request) {

        log.info("Student course enrollment request received for studentId: {} and courseId: {}",
                request.getStudentId(), request.getCourseId());

        StudentCourseResponse response = studentCourseService.enroll(request);

        log.info("Student enrolled successfully for studentId: {} and courseId: {}",
                request.getStudentId(), request.getCourseId());

        return ResponseEntity.ok(response);
    }

    // ================= GET STUDENT COURSES =================
    @GetMapping("/{studentId}")
    public ResponseEntity<List<StudentCourseResponse>> getStudentCourses(
            @PathVariable String studentId) {

        log.info("Fetching enrolled courses for studentId: {}", studentId);

        List<StudentCourseResponse> response =
                studentCourseService.getStudentCourses(studentId);

        log.info("Fetched {} enrolled courses for studentId: {}",
                response.size(), studentId);

        return ResponseEntity.ok(response);
    }

}