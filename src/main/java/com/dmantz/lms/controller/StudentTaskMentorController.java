package com.dmantz.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.AcknowledgeMentorRequest;
import com.dmantz.lms.dto.request.StudentTaskMentorRequest;
import com.dmantz.lms.dto.request.UpdateMentorMinutesRequest;
import com.dmantz.lms.dto.response.StudentTaskMentorResponse;
import com.dmantz.lms.service.StudentTaskMentorService;


@RestController
@RequestMapping("/api/student-task-mentor")
public class StudentTaskMentorController {

    @Autowired
    private StudentTaskMentorService mentorService;

    @PostMapping
    public ResponseEntity<StudentTaskMentorResponse> createMentoringActivity(
            @RequestBody StudentTaskMentorRequest request) {

        StudentTaskMentorResponse response =
                mentorService.createMentoringActivity(request);

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StudentTaskMentorResponse> updateMentoringMinutes(
            @PathVariable Long id,
            @RequestBody UpdateMentorMinutesRequest request) {

        StudentTaskMentorResponse response =
                mentorService.updateMentoringMinutes(id, request);

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<StudentTaskMentorResponse> acknowledgeMentorHelp(
            @PathVariable Long id,
            @RequestBody AcknowledgeMentorRequest request) {

        StudentTaskMentorResponse response =
                mentorService.acknowledgeMentorHelp(id);

        return ResponseEntity.ok(response);
    }

}

