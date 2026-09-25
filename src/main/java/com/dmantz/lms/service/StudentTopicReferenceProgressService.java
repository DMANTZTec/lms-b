package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms.dto.response.StudentTopicReferenceProgressResponse;

public interface StudentTopicReferenceProgressService {

	StudentTopicReferenceProgressResponse markReferenceComplete(StudentTopicReferenceProgressRequest request);
}