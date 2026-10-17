package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.AssignProgramRequest;
import com.dmantz.lms.dto.response.AssignProgramResponse;
import com.dmantz.lms.service.StudentProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student-programs")
public class StudentProgramController {
    private final StudentProgramService studentProgramService;

    public StudentProgramController(StudentProgramService studentProgramService) {
        this.studentProgramService = studentProgramService;
    }

    @PostMapping("/assign")
    public ResponseEntity<AssignProgramResponse> assignProgramToStudent(@RequestBody @Valid AssignProgramRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentProgramService.assignProgramToStudent(request));
    }
}
