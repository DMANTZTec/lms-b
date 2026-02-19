package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;
import com.dmantz.lms_b.service.ClassAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
public class ClassAdminController {

    private final ClassAdminService classAdminService;

    public ClassAdminController(ClassAdminService classAdminService) {
        this.classAdminService = classAdminService;
    }

    @PostMapping("/{courseId}/classes")
    public ResponseEntity<ClassResponse> addClass(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateClassRequest request) {

        ClassResponse response = classAdminService.addClass(courseId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
