package com.dmantz.lms.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms.dto.request.ProviderRequest;
import com.dmantz.lms.dto.response.ProviderResponse;
import com.dmantz.lms.service.ProviderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

	private static final Logger logger =
			LogManager.getLogger(ProviderController.class);

	private final ProviderService providerService;

	public ProviderController(ProviderService providerService) {
		this.providerService = providerService;
	}

	// ================= CREATE =================

	@PostMapping
	public ResponseEntity<ProviderResponse> createProvider(
			@Valid @RequestBody ProviderRequest request,
			@RequestParam String staffId) {

		logger.info("Create provider API called by staffId: {}", staffId);

		ProviderResponse response =
				providerService.createProvider(request, staffId);

		logger.info("Provider created successfully");

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// ================= GET BY ID =================

	@GetMapping("/{id}")
	public ResponseEntity<ProviderResponse> getProviderById(
			@PathVariable Long id) {

		logger.info("Get provider by id API called with id: {}", id);

		ProviderResponse response =
				providerService.getProviderById(id);

		logger.info("Provider fetched successfully with id: {}", id);

		return ResponseEntity.ok(response);
	}

	// ================= GET ALL =================

	@GetMapping
	public ResponseEntity<List<ProviderResponse>> getAllProviders() {

		logger.info("Get all providers API called");

		List<ProviderResponse> providers =
				providerService.getAllProviders();

		logger.info("Total providers fetched: {}", providers.size());

		return ResponseEntity.ok(providers);
	}

	// ================= UPDATE =================

	@PutMapping("/{id}")
	public ResponseEntity<ProviderResponse> updateProvider(
			@PathVariable Long id,
			@Valid @RequestBody ProviderRequest request,
			@RequestParam String staffId) {

		logger.info(
				"Update provider API called for providerId: {} by staffId: {}",
				id,
				staffId
		);

		ProviderResponse response =
				providerService.updateProvider(id, request, staffId);

		logger.info("Provider updated successfully with providerId: {}", id);

		return ResponseEntity.ok(response);
	}

	// ================= DELETE =================

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProvider(
			@PathVariable Long id,
			@RequestParam String staffId) {

		logger.info(
				"Delete provider API called for providerId: {} by staffId: {}",
				id,
				staffId
		);

		providerService.deleteProvider(id, staffId);

		logger.info("Provider deleted successfully with providerId: {}", id);

		return ResponseEntity.ok("Provider deleted successfully");
	}
}