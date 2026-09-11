package com.dmantz.lms.controller;

import java.util.List;

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

import com.dmantz.lms.dto.request.SuccessStoryRequest;
import com.dmantz.lms.dto.response.SuccessStoryResponse;
import com.dmantz.lms.service.SuccessStoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/success-stories")
public class SuccessStoryController {

	  @Autowired
	    private SuccessStoryService successStoryService;

	    // Public endpoint — powers the homepage success stories section
//	    @GetMapping
//	    public ResponseEntity<List<SuccessStoryResponse>> getActiveStories() {
//	        List<SuccessStoryResponse> stories = successStoryService.getActiveStories();
//	        return ResponseEntity.ok(stories);
//	    }
//	    
	    @GetMapping
	    public ResponseEntity<List<SuccessStoryResponse>> getAllStories() {
	        List<SuccessStoryResponse> stories = successStoryService.getAllStories();
	        return ResponseEntity.ok(stories);
	    }

	    @PostMapping
	    public ResponseEntity<SuccessStoryResponse> create(@Valid @RequestBody SuccessStoryRequest request) {
	        SuccessStoryResponse response = successStoryService.create(request);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<SuccessStoryResponse> update(
	            @PathVariable Long id,
	            @Valid @RequestBody SuccessStoryRequest request) {
	        SuccessStoryResponse response = successStoryService.update(id, request);
	        return ResponseEntity.ok(response);
	    }

	    @PatchMapping("/{id}/toggle-active")
	    public ResponseEntity<Void> toggleActive(@PathVariable Long id) {
	        successStoryService.toggleActive(id);
	        return ResponseEntity.noContent().build();
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        successStoryService.delete(id);
	        return ResponseEntity.noContent().build();
	    }
}
