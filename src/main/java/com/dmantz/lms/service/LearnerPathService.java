package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.LearnerPathRequest;
import com.dmantz.lms.dto.response.LearnerPathResponse;

import java.util.List;

public interface LearnerPathService {

    LearnerPathResponse create(LearnerPathRequest request);

    List<LearnerPathResponse> getActivePaths();

    List<LearnerPathResponse> getAllPaths();

    LearnerPathResponse update(Long id, LearnerPathRequest request);

    LearnerPathResponse getById(Long id);

    String delete(Long id);
}