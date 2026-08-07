package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.SocialMediaRequest;
import com.dmantz.lms.dto.response.SocialMediaResponse;
import com.dmantz.lms.entity.SocialMedia;
import com.dmantz.lms.mapper.SocialMediaMapper;
import com.dmantz.lms.repository.SocialMediaRepository;
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

    public SocialMediaServiceImpl(SocialMediaRepository socialMediaRepository,
                                  SocialMediaMapper socialMediaMapper) {
        this.socialMediaRepository = socialMediaRepository;
        this.socialMediaMapper = socialMediaMapper;
    }

    @Override
    public List<SocialMediaResponse> getActiveLinks() {

        logger.info("Fetching active social media links.");

        List<SocialMediaResponse> response = socialMediaRepository.findByIsActiveTrue()
                .stream()
                .sorted(Comparator.comparing(sm -> sm.getPlatform().name()))
                .map(socialMediaMapper::toResponse)
                .toList();

        logger.info("Successfully fetched {} active social media links.", response.size());

        return response;
    }

    @Override
    public List<SocialMediaResponse> getAllLinks() {

        logger.info("Fetching all social media links.");

        List<SocialMediaResponse> response = socialMediaRepository.findAll()
                .stream()
                .map(socialMediaMapper::toResponse)
                .toList();

        logger.info("Successfully fetched {} social media links.", response.size());

        return response;
    }

    @Override
    public SocialMediaResponse createLink(SocialMediaRequest request) {

        logger.info("Creating social media link for platform: {}", request.getPlatform());

        socialMediaRepository.findByPlatform(request.getPlatform()).ifPresent(link -> {
            logger.error("Social media link already exists for platform: {}", request.getPlatform());
            throw new RuntimeException("Social media link already exists for platform: " + request.getPlatform());
        });

        SocialMedia socialMedia = socialMediaMapper.toEntity(request);

        if (socialMedia.getIsActive() == null) {
            socialMedia.setIsActive(true);
        }

        SocialMedia saved = socialMediaRepository.save(socialMedia);

        logger.info("Successfully created social media link with id: {}", saved.getId());

        return socialMediaMapper.toResponse(saved);
    }

    @Override
    public SocialMediaResponse updateLink(Long id, SocialMediaRequest request) {

        logger.info("Updating social media link with id: {}", id);

        SocialMedia socialMedia = socialMediaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Social media link not found with id: {}", id);
                    return new RuntimeException("Social media link not found with id: " + id);
                });

        socialMediaMapper.updateEntity(request, socialMedia);

        SocialMedia updated = socialMediaRepository.save(socialMedia);

        logger.info("Successfully updated social media link with id: {}", id);

        return socialMediaMapper.toResponse(updated);
    }

    @Override
    public void deleteLink(Long id) {

        logger.info("Deleting social media link with id: {}", id);

        if (!socialMediaRepository.existsById(id)) {
            logger.error("Social media link not found with id: {}", id);
            throw new RuntimeException("Social media link not found with id: " + id);
        }

        socialMediaRepository.deleteById(id);

        logger.info("Successfully deleted social media link with id: {}", id);
    }
}