package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.*;
import com.dmantz.lms.mapper.StaffMapper;
import com.dmantz.lms.repository.RoleRepository;
import com.dmantz.lms.repository.StaffOtpRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.StaffService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

	private static final Logger logger = LogManager.getLogger(StaffServiceImpl.class);

	private final StaffRepository staffRepository;
	private final RoleRepository roleRepository;
	private final StaffMapper staffMapper;
	private final PasswordEncoder passwordEncoder;
	private final StaffOtpRepository staffOtpRepository;
	private final EmailService emailService;
	private final JwtUtil jwtUtil;

	public StaffServiceImpl(StaffRepository staffRepository, RoleRepository roleRepository, StaffMapper staffMapper,
			PasswordEncoder passwordEncoder, StaffOtpRepository staffOtpRepository, EmailService emailService) {

		this.staffRepository = staffRepository;
		this.roleRepository = roleRepository;
		this.staffMapper = staffMapper;
		this.passwordEncoder = passwordEncoder;
		this.staffOtpRepository = staffOtpRepository;
		this.emailService = emailService;
		this.jwtUtil = new JwtUtil();
	}

	@Override
	public StaffResponse registerStaff(StaffRegistrationRequest request, Staff loggedInStaff) {

		logger.info("Staff registration started for email: {}", request.getEmailId());

		staffRepository.findByEmailId(request.getEmailId()).ifPresent(s -> {

			logger.error("Email already exists: {}", request.getEmailId());

			throw new DuplicateValuesException("Staff already exists with this email");
		});

		boolean isFirstStaff = staffRepository.count() == 0;

		if (!isFirstStaff) {

			if (loggedInStaff == null) {

				logger.error("Unauthorized staff registration attempt");

				throw new UnauthorizedAccessException("Unauthorized access");
			}

			Staff dbStaff = staffRepository.findById(loggedInStaff.getId()).orElseThrow(() -> {

				logger.error("Logged-in staff not found");

				return new ResourceNotFoundException("Logged-in staff not found");
			});

			boolean isAdmin = dbStaff.getRoles().stream().anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getRoleNm()));

			if (!isAdmin) {

				logger.warn("Non-admin tried to register staff");

				throw new UnauthorizedAccessException("Only ADMIN can register staff");
			}
		}

		if (request.getRoles() == null || request.getRoles().isEmpty()) {

			logger.error("Roles are empty");

			throw new InvalidCredentialsException("At least one role must be provided");
		}

		if (isFirstStaff && request.getRoles().stream().noneMatch(role -> "ADMIN".equalsIgnoreCase(role))) {

			logger.error("First staff must have ADMIN role");

			throw new UnauthorizedAccessException("First staff must have ADMIN role");
		}

		Staff staff = staffMapper.toEntity(request);

		staff.setStaffId(generateStaffId());

		staff.setPassword(passwordEncoder.encode(request.getPassword()));

		staff.setStatus("ACTIVE");
		staff.setEnabled("Y");
		staff.setCreatedDt(LocalDateTime.now());

		if (!isFirstStaff) {

			staff.setCreatedBy(loggedInStaff.getId());
		}

		if (request.getProfileImgBase64() != null && !request.getProfileImgBase64().isBlank()) {

			String base64 = request.getProfileImgBase64();

			if (base64.contains(",")) {

				base64 = base64.substring(base64.indexOf(",") + 1);
			}

			staff.setProfileImg(Base64.getDecoder().decode(base64));
		}

		Set<Role> assignedRoles = request.getRoles().stream().map(String::toUpperCase)
				.map(roleNm -> roleRepository.findByRoleNm(roleNm).orElseThrow(() -> {

					logger.error("Role not found: {}", roleNm);

					return new ResourceNotFoundException(roleNm + " role not found");
				})).collect(Collectors.toSet());

		staff.setRoles(assignedRoles);

		Staff savedStaff = staffRepository.save(staff);

		logger.info("Staff registered successfully with staffId: {}", savedStaff.getStaffId());

		return staffMapper.toResponse(savedStaff);
	}

	private String generateStaffId() {

		Long count = staffRepository.count() + 1;

		return String.format("SF%05d", count);
	}

	@Override
	public Optional<Staff> findByStaffId(String staffId) {

		logger.info("Fetching staff by staffId: {}", staffId);

		return staffRepository.findByStaffId(staffId);
	}

	private StaffOtp generateStaffOtp(String staffId) {

		logger.info("Generating OTP for staffId: {}", staffId);

		Optional<StaffOtp> existingOtp = staffOtpRepository.findTopByStaffIdAndStatusOrderByIdDesc(staffId,
				OtpStatus.NEW);

		if (existingOtp.isPresent()) {

			logger.warn("Existing active OTP found for staffId: {}", staffId);

			return existingOtp.get();
		}

		StaffOtp otp = new StaffOtp();

		otp.setStaffId(staffId);

		otp.setOtp(String.format("%06d", new SecureRandom().nextInt(1_000_000)));

		otp.setStatus(OtpStatus.NEW);
		otp.setAttemptsNum(0);
		otp.setCreatedDt(LocalDateTime.now());

		StaffOtp savedOtp = staffOtpRepository.save(otp);

		logger.info("OTP generated successfully for staffId: {}", staffId);

		return savedOtp;
	}

	@Override
	public StaffLoginResponse verifyStaffOtp(StaffOtpVerifyRequest request) {

		logger.info("OTP verification started for staffId: {}", request.getStaffId());

		StaffOtp otp = staffOtpRepository.findTopByStaffIdOrderByCreatedDtDesc(request.getStaffId()).orElseThrow(() -> {

			logger.error("OTP not found for staffId: {}", request.getStaffId());

			return new OtpNotFoundException("OTP not found");
		});

		// CHECK OTP STATUS
		if (otp.getStatus() != OtpStatus.SENT) {

			logger.warn("Invalid OTP status for staffId: {}", request.getStaffId());

			throw new OtpInvalidException("OTP is not valid");
		}

		// CHECK OTP EXPIRY
		if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {

			otp.setStatus(OtpStatus.EXPIRED);

			otp.setUpdatedDt(LocalDateTime.now());

			staffOtpRepository.save(otp);

			logger.warn("OTP expired for staffId: {}", request.getStaffId());

			throw new OtpExpiredException("OTP expired");
		}

		// INVALID OTP
		if (!otp.getOtp().equals(request.getOtp())) {

			otp.setAttemptsNum(otp.getAttemptsNum() + 1);

			otp.setUpdatedDt(LocalDateTime.now());

			staffOtpRepository.save(otp);

			logger.warn("Invalid OTP entered for staffId: {}", request.getStaffId());

			throw new OtpInvalidException("Invalid OTP");
		}

		// FETCH STAFF
		Staff staff = staffRepository.findByStaffId(request.getStaffId()).orElseThrow(() -> {

			logger.error("Staff not found for staffId: {}", request.getStaffId());

			return new ResourceNotFoundException("Staff not found");
		});

		// GET ROLE
		String role = staff.getRoles().stream().findFirst().map(r -> r.getRoleNm()).orElse("STAFF");

		// GENERATE JWT TOKEN AFTER OTP VERIFICATION
		String token = jwtUtil.generateToken(staff.getEmailId(), role, staff.getStaffId());

		// UPDATE OTP STATUS
		otp.setStatus(OtpStatus.VERIFIED);

		otp.setUpdatedDt(LocalDateTime.now());

		staffOtpRepository.save(otp);

		logger.info("OTP verified successfully for staffId: {}", request.getStaffId());

		// RESPONSE
		StaffLoginResponse response = new StaffLoginResponse();
		response.setStaffId(staff.getStaffId());
		response.setEmail(staff.getEmailId());
		response.setRole("STAFF");
		response.setToken(token);
		response.setMessage("Login successful");
		return response;
	}

	@Override
	public StaffPasswordResponse forgotPassword(ForgotPasswordRequest request) {

//		logger.info("Forgot password started for email: {}", request.getEmail());
//
//		Staff staff = staffRepository.findByEmailId(request.getEmail()).orElseThrow(() -> {
//
//			logger.error("Staff not found for email: {}", request.getEmail());
//
//			return new ResourceNotFoundException("Staff not found");
//		});
//
//		staffOtpRepository.expireActiveOtps(staff.getStaffId(), OtpStatus.EXPIRED,
//				List.of(OtpStatus.NEW, OtpStatus.SENT));
//
//		StaffOtp otp = new StaffOtp();
//
//		otp.setStaffId(staff.getStaffId());
//
//		otp.setOtp(String.valueOf(100000 + new Random().nextInt(900000)));
//
//		otp.setStatus(OtpStatus.SENT);
//		otp.setCreatedDt(LocalDateTime.now());
//
//		staffOtpRepository.save(otp);
//
//		emailService.sendOtpEmail(staff.getEmailId(), otp.getOtp(), OtpPurpose.STAFF_FORGOT_PASSWORD);
//
//		logger.info("Forgot password OTP sent successfully to: {}", staff.getEmailId());
//
//		StaffPasswordResponse response = staffMapper.toPasswordResponse(staff);
//
//		response.setMessage("OTP sent to your registered email.");
//
//		return response;
		return null;
	}

	@Override
	public StaffPasswordResponse resetPassword(StaffResetPasswordRequest request) {

		logger.info("Reset password started for staffId: {}", request.getStaffId());

		StaffOtp otp = staffOtpRepository
				.findTopByStaffIdAndStatusOrderByCreatedDtDesc(request.getStaffId(), OtpStatus.SENT).orElseThrow(() -> {

					logger.error("OTP not found for staffId: {}", request.getStaffId());

					return new OtpNotFoundException("OTP not found or expired");
				});

		if (!otp.getOtp().equals(request.getOtp())) {

			logger.warn("Invalid OTP entered for staffId: {}", request.getStaffId());

			throw new OtpInvalidException("Invalid OTP");
		}

		if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {

			otp.setStatus(OtpStatus.EXPIRED);

			staffOtpRepository.save(otp);

			logger.warn("OTP expired for staffId: {}", request.getStaffId());

			throw new OtpExpiredException("OTP expired");
		}

		Staff staff = staffRepository.findByStaffId(request.getStaffId()).orElseThrow(() -> {

			logger.error("Staff not found for staffId: {}", request.getStaffId());

			return new ResourceNotFoundException("Staff not found");
		});

		staff.setPassword(passwordEncoder.encode(request.getNewPassword()));

		staffRepository.save(staff);

		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());

		staffOtpRepository.save(otp);

		emailService.sendOtpEmail(staff.getEmailId(), null, OtpPurpose.STAFF_PASSWORD_RESET_SUCCESS);

		logger.info("Password reset successful for staffId: {}", request.getStaffId());

		StaffPasswordResponse response = staffMapper.toPasswordResponse(staff);

		response.setMessage("Password reset successful.");

		return response;
	}

	@Override
	public List<StaffResponse> getAllStaff() {

		logger.info("Fetching all staff");

		List<Staff> staffList = staffRepository.findAll();

		if (staffList.isEmpty()) {

			logger.warn("No staff found");

			throw new ResourceNotFoundException("No staff found");
		}

		logger.info("Total staff fetched: {}", staffList.size());

		return staffMapper.toResponseList(staffList);
	}

	@Override
	public StaffResponse getStaffByStaffId(String staffId) {

		logger.info("Fetching staff by staffId: {}", staffId);

		Staff staff = staffRepository.findByStaffId(staffId).orElseThrow(() -> {

			logger.error("Staff not found for staffId: {}", staffId);

			return new ResourceNotFoundException("Staff not found");
		});

		logger.info("Staff fetched successfully for staffId: {}", staffId);

		return staffMapper.toResponse(staff);
	}

	@Override
	public StaffResponse registerInitialAdmin(StaffRegistrationRequest request) {

		logger.info("Initial admin registration started");

		if (staffRepository.count() > 0) {

			logger.error("Initial admin already exists");

			throw new DuplicateValuesException("Initial admin already created");
		}

		if (request.getRoles() == null || request.getRoles().stream().noneMatch(r -> "ADMIN".equalsIgnoreCase(r))) {

			logger.error("Initial admin must have ADMIN role");

			throw new UnauthorizedAccessException("Initial staff must have ADMIN role");
		}

		staffRepository.findByEmailId(request.getEmailId()).ifPresent(s -> {

			logger.error("Email already exists: {}", request.getEmailId());

			throw new DuplicateValuesException("Email already exists");
		});

		Staff staff = staffMapper.toEntity(request);

		staff.setStaffId(generateStaffId());

		staff.setPassword(passwordEncoder.encode(request.getPassword()));

		staff.setStatus("ACTIVE");
		staff.setEnabled("Y");
		staff.setCreatedBy(null);

		if (request.getProfileImgBase64() != null && !request.getProfileImgBase64().isBlank()) {

			String base64 = request.getProfileImgBase64();

			if (base64.contains(",")) {

				base64 = base64.substring(base64.indexOf(",") + 1);
			}

			staff.setProfileImg(Base64.getDecoder().decode(base64));
		}

		Set<Role> roles = request.getRoles().stream().map(String::toUpperCase)
				.map(roleNm -> roleRepository.findByRoleNm(roleNm).orElseThrow(() -> {

					logger.error("Role not found: {}", roleNm);

					return new ResourceNotFoundException(roleNm + " role not found");
				})).collect(Collectors.toSet());

		staff.setRoles(roles);

		Staff savedStaff = staffRepository.save(staff);

		logger.info("Initial admin registered successfully with staffId: {}", savedStaff.getStaffId());

		return staffMapper.toResponse(savedStaff);
	}

}