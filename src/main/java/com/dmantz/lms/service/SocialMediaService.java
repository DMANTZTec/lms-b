package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.SocialMediaRequest;
import com.dmantz.lms.dto.response.SocialMediaResponse;

import java.util.List;

public interface SocialMediaService {

    List<SocialMediaResponse> getActiveLinks();

    List<SocialMediaResponse> getAllLinks();

    SocialMediaResponse createLink(SocialMediaRequest request);

    SocialMediaResponse updateLink(Long id, SocialMediaRequest request);

    void deleteLink(Long id);
}