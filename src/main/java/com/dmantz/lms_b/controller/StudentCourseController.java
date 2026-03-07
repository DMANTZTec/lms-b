package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms_b.dto.response.StudentCourseResponse;
import com.dmantz.lms_b.service.StudentCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-course")
public class StudentCourseController {

    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }


    @PostMapping("/enroll")
    public ResponseEntity<StudentCourseResponse> enroll(
            @RequestBody StudentCourseEnrollRequest request) {

        StudentCourseResponse response = studentCourseService.enroll(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<StudentCourseResponse>> getStudentCourses(
            @PathVariable String studentId) {

        return ResponseEntity.ok(
                studentCourseService.getStudentCourses(studentId));
    }

}
