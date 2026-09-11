package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.LearnerPathRequest;
import com.dmantz.lms.dto.response.LearnerPathResponse;
import com.dmantz.lms.service.LearnerPathService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learner-paths")
public class LearnerPathController {

    @Autowired
    private LearnerPathService learnerPathService;

 
    @GetMapping("/active")
    public ResponseEntity<List<LearnerPathResponse>> getActivePaths() {
        List<LearnerPathResponse> paths = learnerPathService.getActivePaths();
        return ResponseEntity.ok(paths);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LearnerPathResponse>> getAllPaths() {
        List<LearnerPathResponse> paths = learnerPathService.getAllPaths();
        return ResponseEntity.ok(paths);
    }

    @PostMapping
    public ResponseEntity<LearnerPathResponse> create(@Valid @RequestBody LearnerPathRequest request) {
        LearnerPathResponse response = learnerPathService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearnerPathResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LearnerPathRequest request) {
        LearnerPathResponse response = learnerPathService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearnerPathResponse> getById(
            @PathVariable Long id) {

        LearnerPathResponse response =
                learnerPathService.getById(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String message = learnerPathService.delete(id);
        return ResponseEntity.ok(message);
    }
}