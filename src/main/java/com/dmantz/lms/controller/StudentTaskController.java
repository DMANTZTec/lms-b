package com.dmantz.lms.controller;

import com.dmantz.lms.entity.StudentNeedHelpRequest;
import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.service.StudentTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-task")
public class StudentTaskController {

    private final StudentTaskService studentTaskService;

    public StudentTaskController(StudentTaskService studentTaskService) {
        this.studentTaskService = studentTaskService;
    }

    @PostMapping("/addtask")
    public ResponseEntity<StudentTaskResponse> addStudentTask(
            @Valid @RequestBody StudentTaskRequest request) {

        StudentTaskResponse response = studentTaskService.addTask(request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/need-help")
    public ResponseEntity<StudentTaskResponse> markNeedHelp(
            @Valid @RequestBody StudentNeedHelpRequest request) {

        StudentTaskResponse response = studentTaskService.updateNeedHelp(request);

        return ResponseEntity.ok(response);
    }
}
