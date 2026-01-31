package com.dmantz.lms_b.service;

import java.util.List;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;

public interface CourseManagementService {
//	create a subject
	SubjectResponse createSubject(SubjectRequest requestDto, Long staffID);
	
//  view all subject
	List<SubjectResponse> viewAllSubjects();
	
// update an existing subject
	SubjectResponse updateSubject(Long subjectId, SubjectRequest request,Long staffId);
	
	
//	delete subject
	SubjectResponse deleteSubject(Long subjectId, Long staffId);

}
