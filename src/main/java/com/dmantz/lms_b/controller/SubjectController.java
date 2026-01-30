package com.dmantz.lms_b.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.service.SubjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

	private final SubjectService subjectservice;

	public SubjectController(SubjectService subjectservice) {
		super();
		this.subjectservice = subjectservice;
	}

	@PostMapping
	public SubjectResponse createSubject(@Valid @RequestBody SubjectRequest requestDto, @RequestParam Long staffId) {
		return subjectservice.createSubject(requestDto, staffId);

	}

	@GetMapping
	public List<SubjectResponse> getAllSubjects() {
		return subjectservice.getAllSubjects();
	}

}
