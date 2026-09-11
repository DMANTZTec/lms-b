package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.ProviderRequest;
import com.dmantz.lms.dto.response.ProviderResponse;
import com.dmantz.lms.entity.Provider;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.ProviderMapper;
import com.dmantz.lms.repository.ProviderRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.ProviderService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

	private static final Logger logger =
			LogManager.getLogger(ProviderServiceImpl.class);

	private final ProviderRepository providerRepository;
	private final ProviderMapper providerMapper;
	private final StaffRepository staffRepository;

	public ProviderServiceImpl(ProviderRepository providerRepository,
							   ProviderMapper providerMapper,
							   StaffRepository staffRepository) {

		this.providerRepository = providerRepository;
		this.providerMapper = providerMapper;
		this.staffRepository = staffRepository;
	}

	// ================= CREATE =================
	@Override
	public ProviderResponse createProvider(ProviderRequest request, String staffId) {

		logger.info("Provider creation started by staffId: {}", staffId);

		staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> {
					logger.error("Staff not found with id: {}", staffId);
					return new ResourceNotFoundException(
							"Staff not found with id: " + staffId);
				});

		if (providerRepository.existsByProviderName(request.getProviderName())) {

			logger.warn("Provider name already exists: {}",
					request.getProviderName());

			throw new DuplicateValuesException(
					"Provider name already exists");
		}

		Provider provider = providerMapper.toEntity(request);

		Provider saved = providerRepository.save(provider);

		logger.info("Provider created successfully with providerId: {}",
				saved.getId());

		return providerMapper.toResponse(saved);
	}

	// ================= GET BY ID =================
	@Override
	public ProviderResponse getProviderById(Long id) {

		logger.info("Fetching provider with id: {}", id);

		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> {

					logger.error("Provider not found with id: {}", id);

					return new ResourceNotFoundException(
							"Provider not found with id: " + id);
				});

		logger.info("Provider fetched successfully with id: {}", id);

		return providerMapper.toResponse(provider);
	}

	// ================= GET ALL =================
	@Override
	public List<ProviderResponse> getAllProviders() {

		logger.info("Fetching all providers");

		List<ProviderResponse> providers = providerRepository.findAll()
				.stream()
				.map(providerMapper::toResponse)
				.toList();

		logger.info("Total providers fetched: {}", providers.size());

		return providers;
	}

	// ================= UPDATE =================
	@Override
	public ProviderResponse updateProvider(Long providerId,
										   ProviderRequest request,
										   String staffId) {

		logger.info("Updating provider with providerId: {} by staffId: {}",
				providerId, staffId);

		staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> {

					logger.error("Staff not found with id: {}", staffId);

					return new ResourceNotFoundException(
							"Staff not found with id: " + staffId);
				});

		Provider provider = providerRepository.findById(providerId)
				.orElseThrow(() -> {

					logger.error("Provider not found with id: {}",
							providerId);

					return new ResourceNotFoundException(
							"Provider not found with id: " + providerId);
				});

		if (providerRepository.existsByProviderNameAndIdNot(
				request.getProviderName(),
				providerId)) {

			logger.warn("Duplicate provider name found: {}",
					request.getProviderName());

			throw new DuplicateValuesException(
					"Provider name already exists");
		}

		providerMapper.updateEntityFromRequest(request, provider);

		Provider updated = providerRepository.save(provider);

		logger.info("Provider updated successfully with providerId: {}",
				updated.getId());

		return providerMapper.toResponse(updated);
	}

	// ================= DELETE BY ID =================
	@Override
	public void deleteProvider(Long providerId, String staffId) {

		logger.info("Deleting provider with providerId: {} by staffId: {}",
				providerId, staffId);

		staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> {

					logger.error("Staff not found with id: {}", staffId);

					return new ResourceNotFoundException(
							"Staff not found with id: " + staffId);
				});

		Provider provider = providerRepository.findById(providerId)
				.orElseThrow(() -> {

					logger.error("Provider not found with id: {}",
							providerId);

					return new ResourceNotFoundException(
							"Provider not found with id: " + providerId);
				});

		providerRepository.delete(provider);

		logger.info("Provider deleted successfully with providerId: {}",
				providerId);
	}
}