package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.CourseFeeRequest;
import com.dmantz.lms.dto.response.CourseFeeHistoryResponse;
import com.dmantz.lms.dto.response.CourseFeeSettingResponse;

public interface CourseFeeService {

    CourseFeeSettingResponse getCourseFeeSetting(String courseId);

    CourseFeeHistoryResponse createCourseFee(String courseId, CourseFeeRequest request, String staffId);

    CourseFeeSettingResponse updateCourseFee(String courseId, CourseFeeRequest request, String staffId);

    List<CourseFeeHistoryResponse> getFeeHistory(String courseId);
}