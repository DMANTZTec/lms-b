package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.SocialMediaRequest;
import com.dmantz.lms.dto.response.SocialMediaResponse;
import com.dmantz.lms.service.SocialMediaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social-media")
public class SocialMediaController {

    private static final Logger logger = LoggerFactory.getLogger(SocialMediaController.class);

    private final SocialMediaService socialMediaService;

    public SocialMediaController(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<SocialMediaResponse>> getActiveLinks() {
        logger.info("Received request to fetch active social media links.");

        List<SocialMediaResponse> response = socialMediaService.getActiveLinks();

        logger.info("Successfully fetched {} active social media links.", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SocialMediaResponse>> getAllLinks() {
        logger.info("Received request to fetch all social media links.");

        List<SocialMediaResponse> response = socialMediaService.getAllLinks();

        logger.info("Successfully fetched {} social media links.", response.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SocialMediaResponse> createLink(
            @Valid @RequestBody SocialMediaRequest request) {

        logger.info("Received request to create social media link for platform: {}", request.getPlatform());

        SocialMediaResponse response = socialMediaService.createLink(request);

        logger.info("Successfully created social media link for platform: {}", response.getPlatform());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocialMediaResponse> updateLink(
            @PathVariable Long id,
            @Valid @RequestBody SocialMediaRequest request) {

        logger.info("Received request to update social media link with id: {}", id);

        SocialMediaResponse response = socialMediaService.updateLink(id, request);

        logger.info("Successfully updated social media link with id: {}", id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLink(@PathVariable Long id) {

        logger.info("Received request to delete social media link with id: {}", id);

        socialMediaService.deleteLink(id);

        logger.info("Successfully deleted social media link with id: {}", id);

        return ResponseEntity.ok("Social media link deleted successfully.");
    }
}