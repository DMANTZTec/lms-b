package com.dmantz.lms.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.CourseFeeRequest;
import com.dmantz.lms.dto.response.CourseFeeHistoryResponse;
import com.dmantz.lms.dto.response.CourseFeeSettingResponse;
import com.dmantz.lms.service.CourseFeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/course-fee")
public class CourseFeeController {

    private static final Logger logger = LogManager.getLogger(CourseFeeController.class);

    private final CourseFeeService courseFeeService;

    public CourseFeeController(CourseFeeService courseFeeService) {
        this.courseFeeService = courseFeeService;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseFeeSettingResponse> getCourseFeeSetting(@PathVariable String courseId) {
        logger.info("GET /api/course-fee/{} - Fetching course fee setting", courseId);
        return ResponseEntity.ok(courseFeeService.getCourseFeeSetting(courseId));
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<CourseFeeHistoryResponse> createCourseFee(
            @PathVariable String courseId,
            @RequestParam String staffId,
            @Valid @RequestBody CourseFeeRequest request) {

        logger.info("POST /api/course-fee/{} - Creating initial fee by staffId: {}", courseId, staffId);
        CourseFeeHistoryResponse response = courseFeeService.createCourseFee(courseId, request, staffId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseFeeSettingResponse> updateCourseFee(
            @PathVariable String courseId,
            @RequestParam String staffId,
            @Valid @RequestBody CourseFeeRequest request) {

        logger.info("PUT /api/course-fee/{} - Adding new fee version by staffId: {}", courseId, staffId);
        return ResponseEntity.ok(courseFeeService.updateCourseFee(courseId, request, staffId));
    }

    @GetMapping("/{courseId}/history")
    public ResponseEntity<List<CourseFeeHistoryResponse>> getFeeHistory(@PathVariable String courseId) {
        logger.info("GET /api/course-fee/{}/history - Fetching fee history", courseId);
        return ResponseEntity.ok(courseFeeService.getFeeHistory(courseId));
    }
}