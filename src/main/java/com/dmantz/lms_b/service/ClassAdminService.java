package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;

public interface ClassAdminService {

   ClassResponse addClass(Long courseId, CreateClassRequest request);

}
