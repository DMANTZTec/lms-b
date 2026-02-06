package com.dmantz.lms_b.service;

import java.util.List;

import com.dmantz.lms_b.dto.request.ProviderRequest;
import com.dmantz.lms_b.dto.response.ProviderResponse;

public interface ProviderService {

	ProviderResponse createProvider(ProviderRequest request, Long staffId);

	ProviderResponse getProviderById(Long id);

	List<ProviderResponse> getAllProviders();

	ProviderResponse updateProvider(Long providerId, ProviderRequest request, Long staffId);

	void deleteProvider(Long providerId, Long staffId);

}
