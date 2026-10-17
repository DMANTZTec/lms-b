package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.ProviderRequest;
import com.dmantz.lms.dto.response.ProviderResponse;

public interface ProviderService {

	ProviderResponse createProvider(ProviderRequest request, String staffId);

	ProviderResponse getProviderById(Long id);

	List<ProviderResponse> getAllProviders();

	ProviderResponse updateProvider(Long providerId, ProviderRequest request, String staffId);

	void deleteProvider(Long providerId, String staffId);

}
