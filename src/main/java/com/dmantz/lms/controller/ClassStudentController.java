package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.EnrollStudentRequest;
import com.dmantz.lms.dto.request.RemoveStudentRequest;
import com.dmantz.lms.dto.response.EnrollStudentResponse;
import com.dmantz.lms.service.ClassStudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/class-students")
public class ClassStudentController {

    private final ClassStudentService classStudentService;

    public ClassStudentController(ClassStudentService classStudentService) {
        this.classStudentService = classStudentService;
    }

    //  Enroll (Self + Staff in single API)
    @PostMapping("/enroll")
    public ResponseEntity<List<EnrollStudentResponse>> enrollStudents(
            @Valid @RequestBody EnrollStudentRequest request) {

        List<EnrollStudentResponse> response =
                classStudentService.enrollStudents(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/remove")
    public ResponseEntity<List<String>> removeStudents(@RequestBody RemoveStudentRequest request) {

        List<String> removedStudents = classStudentService.removeStudents(request);
        return ResponseEntity.ok(removedStudents);
    }

}