package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.StudentTaskSubmissionRequest;
import com.dmantz.lms.dto.response.StudentTaskSubmissionResponse;

public interface StudentTaskSubmissionService {
	
	StudentTaskSubmissionResponse submitTask(StudentTaskSubmissionRequest request);
}
