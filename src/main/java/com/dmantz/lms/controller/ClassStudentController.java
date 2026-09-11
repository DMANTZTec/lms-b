package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.EnrollStudentRequest;
import com.dmantz.lms.dto.request.RemoveStudentRequest;
import com.dmantz.lms.dto.response.EnrollStudentResponse;
import com.dmantz.lms.service.ClassStudentService;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	 private static final Logger logger = LogManager.getLogger(ClassStudentController.class);


    private final ClassStudentService classStudentService;

    public ClassStudentController(ClassStudentService classStudentService) {
        this.classStudentService = classStudentService;
    }

    //  Enroll (Self + Staff in single API)
    @PostMapping("/enroll")
    public ResponseEntity<List<EnrollStudentResponse>> enrollStudents(
            @Valid @RequestBody EnrollStudentRequest request) {

    	logger.info("POST /enroll - Enrolling student(s) into classBatchId: {} — selfEnroll: {}",
                request.getClassBatchId(), request.isSelfEnroll());

        List<EnrollStudentResponse> response = classStudentService.enrollStudents(request);

        logger.info("Enrollment successful for classBatchId: {} — {} student(s) enrolled",
                request.getClassBatchId(), response.size());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/remove")
    public ResponseEntity<List<String>> removeStudents(@RequestBody RemoveStudentRequest request) {

    	logger.info("POST /remove - Removing student(s) from classBatchId: {} — selfRemove: {}",
                request.getClassBatchId(), request.isSelfRemove());

        List<String> removedStudents = classStudentService.removeStudents(request);

        logger.info("Removal successful for classBatchId: {} — {} student(s) removed",
                request.getClassBatchId(), removedStudents.size());

        return ResponseEntity.ok(removedStudents);
    }

}