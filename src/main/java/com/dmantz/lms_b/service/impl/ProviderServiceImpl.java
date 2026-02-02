package com.dmantz.lms_b.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dmantz.lms_b.dto.request.ProviderRequest;
import com.dmantz.lms_b.dto.response.ProviderResponse;
import com.dmantz.lms_b.entity.Provider;
import com.dmantz.lms_b.exceptions.DuplicateValuesException;
import com.dmantz.lms_b.exceptions.ResourceNotFoundException;
import com.dmantz.lms_b.mapper.ProviderMapper;
import com.dmantz.lms_b.repository.ProviderRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.service.ProviderService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

	private final ProviderRepository providerRepository;
	private final ProviderMapper providerMapper;
	private final StaffRepository staffRepository;

	public ProviderServiceImpl(ProviderRepository providerRepository, ProviderMapper providerMapper,
			StaffRepository staffRepository) {
		this.providerRepository = providerRepository;
		this.providerMapper = providerMapper;
		this.staffRepository = staffRepository;
	}

	// ================= CREATE =================
	@Override
	public ProviderResponse createProvider(ProviderRequest request, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		if (providerRepository.existsByProviderName(request.getProviderName())) {
			throw new DuplicateValuesException("Provider name already exists");
		}

		Provider provider = providerMapper.toEntity(request);
		provider.setCreated_by(staffId);
		provider.setCreated_dt(LocalDateTime.now());
		
		Provider saved = providerRepository.save(provider);
		return providerMapper.toResponse(saved);
	}

	// ================= GET BY ID =================
	@Override
	public ProviderResponse getProviderById(Long id) {
		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + id));

		return providerMapper.toResponse(provider);
	}

	// ================= GET ALL =================
	@Override
	public List<ProviderResponse> getAllProviders() {
		return providerRepository.findAll().stream().map(providerMapper::toResponse).toList();
	}

	// ================= UPDATE =================
	@Override
	public ProviderResponse updateProvider(Long providerId, ProviderRequest request, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Provider provider = providerRepository.findById(providerId)
				.orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));

		if (providerRepository.existsByProviderNameAndNotId(request.getProviderName(), providerId)) {
			throw new DuplicateValuesException("Provider name already exists");
		}

		providerMapper.updateEntityFromDto(request, provider);
		provider.setUpdated_by(staffId);
		provider.setUpdated_dt(LocalDateTime.now());

		Provider updated = providerRepository.save(provider);
		return providerMapper.toResponse(updated);
	}

	// ================= DELETE BY ID =================
	@Override
	public void deleteProvider(Long providerId, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Provider provider = providerRepository.findById(providerId)
				.orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));

		providerRepository.delete(provider);
	}

	// ================= DELETE ALL =================
	@Override
	public void deleteAllProviders(Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		if (providerRepository.count() == 0) {
			throw new ResourceNotFoundException("No providers found to delete");
		}

		providerRepository.deleteAll();
	}
}
