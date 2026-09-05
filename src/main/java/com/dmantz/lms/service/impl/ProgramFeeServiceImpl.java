package com.dmantz.lms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.ProgramFeeRequest;
import com.dmantz.lms.dto.response.ProgramFeeHistoryResponse;
import com.dmantz.lms.dto.response.ProgramFeeSettingResponse;
import com.dmantz.lms.entity.Program;
import com.dmantz.lms.entity.ProgramFee;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.ProgramFeeMapper;
import com.dmantz.lms.repository.ProgramFeeRepository;
import com.dmantz.lms.repository.ProgramRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.ProgramFeeService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProgramFeeServiceImpl implements ProgramFeeService {

	private static final Logger logger = LogManager.getLogger(ProgramFeeServiceImpl.class);

	private final ProgramRepository programRepository;
	private final ProgramFeeRepository programFeeRepository;
	private final StaffRepository staffRepository;
	private final ProgramFeeMapper programFeeMapper;

	public ProgramFeeServiceImpl(ProgramRepository programRepository, ProgramFeeRepository programFeeRepository,
			StaffRepository staffRepository, ProgramFeeMapper programFeeMapper) {
		this.programRepository = programRepository;
		this.programFeeRepository = programFeeRepository;
		this.staffRepository = staffRepository;
		this.programFeeMapper = programFeeMapper;
	}

	@Override
	public ProgramFeeSettingResponse getProgramFeeSetting(String programId) {
		logger.info("Fetching program fee setting for programId: {}", programId);

		Program program = programRepository.findByProgramId(programId).orElseThrow(() -> {
			logger.error("Program not found with programId: {}", programId);
			return new ResourceNotFoundException("Program not found with programId: " + programId);
		});

		List<ProgramFee> feeRecords = programFeeRepository.findByProgram_IdOrderByEffectiveDateAsc(program.getId());

		logger.info("Successfully fetched program fee setting for programId: {}", programId);

		return programFeeMapper.toSettingResponse(program, feeRecords);
	}

	@Override
	public ProgramFeeHistoryResponse createProgramFee(String programId, ProgramFeeRequest request, String staffId) {
		logger.info("Creating initial program fee for programId: {} by staffId: {}", programId, staffId);

		// Validate Staff
		Staff staff = staffRepository.findByStaffId(staffId).orElseThrow(() -> {
			logger.error("Staff not found with staffId: {}", staffId);
			return new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		});

		// Validate Program
		Program program = programRepository.findByProgramId(programId).orElseThrow(() -> {
			logger.error("Program not found with programId: {}", programId);
			return new ResourceNotFoundException("Program not found with programId: " + programId);
		});

		// Validate Discount
		BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;

		if (discount.compareTo(request.getFee()) > 0) {
			logger.error("Discount {} is greater than fee {} for programId: {}", discount, request.getFee(), programId);
			throw new IllegalArgumentException("Discount cannot be greater than fee");
		}

		// Check if fee already exists
		if (programFeeRepository.findTopByProgram_IdOrderByEffectiveDateDesc(program.getId()).isPresent()) {
			logger.warn("Program fee already exists for programId: {}", programId);
			throw new DuplicateValuesException("Program fee already exists. Use update API to add a new fee version.");
		}

		// Upsert — update if exists for same date, else create new
		Optional<ProgramFee> existing = programFeeRepository
		        .findByProgram_IdAndEffectiveDate(program.getId(), request.getEffectiveDate());

		ProgramFee programFee;

		if (existing.isPresent()) {
		    logger.info("Fee exists for effective date: {}, updating it", request.getEffectiveDate());
		    programFee = existing.get();
		} else {
		    programFee = new ProgramFee();
		    programFee.setProgram(program);
		    programFee.setEffectiveDate(request.getEffectiveDate());
		}

		programFee.setFee(request.getFee());
		programFee.setDiscount(discount);
		programFee.setDuration(request.getDuration());
		programFee.setSetBy(staff);

		
		programFee.setFee(request.getFee());
		programFee.setDiscount(discount);
		programFee.setDuration(request.getDuration());
		programFee.setSetBy(staff);

		ProgramFee savedFee = programFeeRepository.save(programFee);

		logger.info("Program fee created successfully for programId: {} with feeId: {}", programId, savedFee.getId());

		return programFeeMapper.toHistoryResponse(savedFee);
	}

	@Override
	public ProgramFeeSettingResponse updateProgramFee(String programId, ProgramFeeRequest request, String staffId) {
		logger.info("Updating program fee for programId: {} by staffId: {}", programId, staffId);

		// Validate Staff
		Staff staff = staffRepository.findByStaffId(staffId).orElseThrow(() -> {
			logger.error("Staff not found with staffId: {}", staffId);
			return new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		});

		// Validate Program
		Program program = programRepository.findByProgramId(programId).orElseThrow(() -> {
			logger.error("Program not found with programId: {}", programId);
			return new ResourceNotFoundException("Program not found with programId: " + programId);
		});

		// Validate Discount
		BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;

		if (discount.compareTo(request.getFee()) > 0) {
			logger.error("Discount {} is greater than fee {} for programId: {}", discount, request.getFee(), programId);
			throw new IllegalArgumentException("Discount cannot be greater than fee");
		}

		// Check duplicate effective date
		if (programFeeRepository.existsByProgram_IdAndEffectiveDate(program.getId(), request.getEffectiveDate())) {
			logger.warn("Fee already exists for effective date: {} and programId: {}", request.getEffectiveDate(),
					programId);
			throw new DuplicateValuesException("Fee already exists for effective date: " + request.getEffectiveDate());
		}

		// Create new fee version
		ProgramFee programFee = new ProgramFee();
		programFee.setProgram(program);
		programFee.setEffectiveDate(request.getEffectiveDate());
		programFee.setFee(request.getFee());
		programFee.setDiscount(discount);
		programFee.setDuration(request.getDuration());
		programFee.setSetBy(staff);

		programFeeRepository.save(programFee);

		logger.info("New program fee version added successfully for programId: {}", programId);

		List<ProgramFee> feeRecords = programFeeRepository.findByProgram_IdOrderByEffectiveDateAsc(program.getId());

		logger.info("Program fee updated successfully for programId: {}", programId);

		return programFeeMapper.toSettingResponse(program, feeRecords);
	}

	@Override
	public List<ProgramFeeHistoryResponse> getFeeHistory(String programId) {
		logger.info("Fetching fee history for programId: {}", programId);

		Program program = programRepository.findByProgramId(programId).orElseThrow(() -> {
			logger.error("Program not found with programId: {}", programId);
			return new ResourceNotFoundException("Program not found with programId: " + programId);
		});

		List<ProgramFee> feeRecords = programFeeRepository.findByProgram_IdOrderByEffectiveDateAsc(program.getId());

		if (feeRecords.isEmpty()) {
			logger.warn("No fee history found for programId: {}", programId);
			return Collections.emptyList();
		}

		List<ProgramFeeHistoryResponse> history = new ArrayList<>();
		for (ProgramFee feeRecord : feeRecords) {
			history.add(programFeeMapper.toHistoryResponse(feeRecord));
		}

		logger.info("Successfully fetched {} fee history record(s) for programId: {}", history.size(), programId);

		return history;
	}
}