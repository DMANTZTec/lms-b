package com.dmantz.lms_b.service;

import java.util.List;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;

public interface SubjectService {

	SubjectResponse createSubject(SubjectRequest requestDto, Long staffID);

	List<SubjectResponse> getAllSubjects();

}
