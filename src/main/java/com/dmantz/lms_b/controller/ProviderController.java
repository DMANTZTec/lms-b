package com.dmantz.lms_b.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dmantz.lms_b.dto.request.ProviderRequest;
import com.dmantz.lms_b.dto.response.ProviderResponse;
import com.dmantz.lms_b.service.ProviderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

	private final ProviderService providerService;

	public ProviderController(ProviderService providerService) {
		this.providerService = providerService;
	}

	@PostMapping
	public ResponseEntity<ProviderResponse> createProvider(@Valid @RequestBody ProviderRequest request,
			@RequestParam Long staffId) {

		ProviderResponse response = providerService.createProvider(request, staffId);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProviderResponse> getProviderById(@PathVariable Long id) {

		return ResponseEntity.ok(providerService.getProviderById(id));
	}

	@GetMapping
	public ResponseEntity<List<ProviderResponse>> getAllProviders() {
		return ResponseEntity.ok(providerService.getAllProviders());
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProviderResponse> updateProvider(@PathVariable Long id,
			@Valid @RequestBody ProviderRequest request, @RequestParam Long staffId) {

		ProviderResponse response = providerService.updateProvider(id, request, staffId);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProvider(
	        @PathVariable Long id,
	        @RequestParam Long staffId) {

	    providerService.deleteProvider(id, staffId);
	    return ResponseEntity.ok("Provider deleted successfully");
	}


}
