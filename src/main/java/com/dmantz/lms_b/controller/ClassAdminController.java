package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.request.UpdateClassRequest;
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
            @PathVariable String courseId,
            @RequestBody CreateClassRequest request) {

        return ResponseEntity.ok(classAdminService.addClass(courseId, request));
    }

    @PutMapping("/classes/{batchId}")
    public ResponseEntity<ClassResponse> modifyClass(
            @PathVariable Long batchId,
            @RequestBody UpdateClassRequest request) {

        return ResponseEntity.ok(classAdminService.modifyClass(batchId, request));
    }


    @PatchMapping("/classes/{batchId}/cancel")
    public ResponseEntity<ClassResponse> cancelClass(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(classAdminService.cancelClass(batchId));
    }

}
