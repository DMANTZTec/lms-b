package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.request.UpdateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;

public interface ClassAdminService {

   ClassResponse addClass(String courseId, CreateClassRequest request);

   ClassResponse modifyClass(Long batchId, UpdateClassRequest request);

   ClassResponse cancelClass(Long batchId);
}
