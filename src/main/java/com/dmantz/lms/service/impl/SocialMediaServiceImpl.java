package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.SocialMediaRequest;
import com.dmantz.lms.dto.response.SocialMediaResponse;
import com.dmantz.lms.entity.SocialMedia;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.SocialMediaMapper;
import com.dmantz.lms.repository.SocialMediaRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.SocialMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SocialMediaServiceImpl implements SocialMediaService {

	private static final Logger logger = LoggerFactory.getLogger(SocialMediaServiceImpl.class);

	private final SocialMediaRepository socialMediaRepository;
	private final SocialMediaMapper socialMediaMapper;
	private final StaffRepository staffRepository;

	
	public SocialMediaServiceImpl(SocialMediaRepository socialMediaRepository, SocialMediaMapper socialMediaMapper,
			StaffRepository staffRepository) {
		super();
		this.socialMediaRepository = socialMediaRepository;
		this.socialMediaMapper = socialMediaMapper;
		this.staffRepository = staffRepository;
	}

	@Override
	public List<SocialMediaResponse> getActiveLinks() {

		logger.info("Fetching active social media links.");

		List<SocialMediaResponse> response = socialMediaRepository.findByIsActiveTrue().stream()
				.sorted(Comparator.comparing(sm -> sm.getPlatform().name())).map(socialMediaMapper::toResponse)
				.toList();

		logger.info("Successfully fetched {} active social media links.", response.size());

		return response;
	}

	@Override
	public List<SocialMediaResponse> getAllLinks() {

		logger.info("Fetching all social media links.");

		List<SocialMediaResponse> response = socialMediaRepository.findAll().stream().map(socialMediaMapper::toResponse)
				.toList();

		logger.info("Successfully fetched {} social media links.", response.size());

		return response;
	}

	@Override
	public SocialMediaResponse createLink(String staffId, SocialMediaRequest request) {

		logger.info("Creating social media link. Staff ID: {}, Platform: {}", staffId, request.getPlatform());

		if (staffId == null || staffId.trim().isEmpty()) {

			logger.error("Staff ID is null or empty.");

			throw new ResourceNotFoundException("Staff ID is required.");
		}

		boolean staffExists = staffRepository.existsByStaffId(staffId);

		if (!staffExists) {

			logger.error("Staff not found with staffId: {}", staffId);

			throw new ResourceNotFoundException("Staff not found with staffId: " + staffId);
		}

		logger.info("Staff validated successfully with staffId: {}", staffId);

		socialMediaRepository.findByPlatform(request.getPlatform()).ifPresent(link -> {

			logger.error("Social media link already exists for platform: {}", request.getPlatform());

			throw new DuplicateValuesException(
					"Social media link already exists for platform: " + request.getPlatform());
		});

		SocialMedia socialMedia = socialMediaMapper.toEntity(request);

		if (socialMedia.getIsActive() == null) {
			socialMedia.setIsActive(true);
		}

		SocialMedia saved = socialMediaRepository.save(socialMedia);

		logger.info("Successfully created social media link with id: {} by staffId: {}", saved.getId(), staffId);

		return socialMediaMapper.toResponse(saved);
	}

	@Override
	public SocialMediaResponse updateLink(Long id, String staffId, SocialMediaRequest request) {

		logger.info("Updating social media link. ID: {}, Staff ID: {}, Platform: {}", id, staffId,
				request.getPlatform());

		if (staffId == null || staffId.trim().isEmpty()) {

			logger.error("Staff ID is null or empty.");

			throw new ResourceNotFoundException("Staff ID is required.");
		}

		boolean staffExists = staffRepository.existsByStaffId(staffId);

		if (!staffExists) {

			logger.error("Staff not found with staffId: {}", staffId);

			throw new ResourceNotFoundException("Staff not found with staffId: " + staffId);
		}

		logger.info("Staff validated successfully with staffId: {}", staffId);

		SocialMedia socialMedia = socialMediaRepository.findById(id).orElseThrow(() -> {

			logger.error("Social media link not found with id: {}", id);

			return new ResourceNotFoundException("Social media link not found with id: " + id);
		});
		socialMediaRepository.findByPlatform(request.getPlatform()).ifPresent(existingLink -> {

			if (!existingLink.getId().equals(id)) {

				logger.error("Social media link already exists for platform: {}", request.getPlatform());

				throw new DuplicateValuesException(
						"Social media link already exists for platform: " + request.getPlatform());
			}
		});

		socialMediaMapper.updateEntity(request, socialMedia);

		SocialMedia updated = socialMediaRepository.save(socialMedia);

		logger.info("Successfully updated social media link with id: {} by staffId: {}", id, staffId);

		return socialMediaMapper.toResponse(updated);
	}

	@Override
	public void deleteLink(Long id, String staffId) {

		logger.info("Deleting social media link. ID: {}, Staff ID: {}", id, staffId);

		if (staffId == null || staffId.trim().isEmpty()) {

			logger.error("Staff ID is null or empty.");

			throw new ResourceNotFoundException("Staff ID is required.");
		}

		boolean staffExists = staffRepository.existsByStaffId(staffId);

		if (!staffExists) {

			logger.error("Staff not found with staffId: {}", staffId);

			throw new ResourceNotFoundException("Staff not found with staffId: " + staffId);
		}

		logger.info("Staff validated successfully with staffId: {}", staffId);

		SocialMedia socialMedia = socialMediaRepository.findById(id).orElseThrow(() -> {

			logger.error("Social media link not found with id: {}", id);

			return new ResourceNotFoundException("Social media link not found with id: " + id);
		});

		socialMediaRepository.delete(socialMedia);

		logger.info("Successfully deleted social media link with id: {} by staffId: {}", id, staffId);
	}
}