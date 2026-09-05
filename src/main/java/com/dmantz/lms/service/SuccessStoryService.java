package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.SuccessStoryRequest;
import com.dmantz.lms.dto.response.SuccessStoryResponse;

public interface SuccessStoryService {

    SuccessStoryResponse create(SuccessStoryRequest request);

    List<SuccessStoryResponse> getActiveStories();

    List<SuccessStoryResponse> getAllStories();

    SuccessStoryResponse update(Long id, SuccessStoryRequest request);

    void toggleActive(Long id);

    void delete(Long id);

}