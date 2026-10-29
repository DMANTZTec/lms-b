package com.dmantz.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.ProgramFeeRequest;
import com.dmantz.lms.dto.response.ProgramFeeHistoryResponse;
import com.dmantz.lms.dto.response.ProgramFeeSettingResponse;
import com.dmantz.lms.service.ProgramFeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/program-fee")
public class ProgramFeeController {

	private final ProgramFeeService programFeeService;

	public ProgramFeeController(ProgramFeeService programFeeService) {
		this.programFeeService = programFeeService;
	}

	@GetMapping("/{programId}")
	public ResponseEntity<ProgramFeeSettingResponse> getProgramFeeSetting(@PathVariable String programId) {
		return ResponseEntity.ok(programFeeService.getProgramFeeSetting(programId));
	}

	@PostMapping("/{programId}")
	public ResponseEntity<ProgramFeeHistoryResponse> createProgramFee(@PathVariable String programId,
			@Valid @RequestBody ProgramFeeRequest request, @RequestParam String staffId) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(programFeeService.createProgramFee(programId, request, staffId));
	}

	@PutMapping("/{programId}")
	public ResponseEntity<ProgramFeeSettingResponse> updateProgramFee(@PathVariable String programId,
			@Valid @RequestBody ProgramFeeRequest request, @RequestParam String staffId) {
		return ResponseEntity.ok(programFeeService.updateProgramFee(programId, request, staffId));
	}

	@GetMapping("/{programId}/history")
	public ResponseEntity<List<ProgramFeeHistoryResponse>> getFeeHistory(@PathVariable String programId) {
		return ResponseEntity.ok(programFeeService.getFeeHistory(programId));
	}
}