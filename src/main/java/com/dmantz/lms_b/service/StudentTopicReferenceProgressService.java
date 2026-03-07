package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms_b.dto.response.StudentTopicReferenceProgressResponse;

public interface StudentTopicReferenceProgressService {

	StudentTopicReferenceProgressResponse markReferenceComplete(StudentTopicReferenceProgressRequest request);
}