package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.*;
import com.dmantz.lms.mapper.StaffMapper;
import com.dmantz.lms.repository.RoleRepository;
import com.dmantz.lms.repository.StaffOtpRepository;
import com.dmantz.lms.repository.StaffPasswordTokenRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.StaffService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
	private final StaffPasswordTokenRepository staffPasswordTokenRepository;

	public StaffServiceImpl(StaffRepository staffRepository, RoleRepository roleRepository, StaffMapper staffMapper,
                            PasswordEncoder passwordEncoder, StaffOtpRepository staffOtpRepository, EmailService emailService, StaffPasswordTokenRepository staffPasswordTokenRepository) {

		this.staffRepository = staffRepository;
		this.roleRepository = roleRepository;
		this.staffMapper = staffMapper;
		this.passwordEncoder = passwordEncoder;
		this.staffOtpRepository = staffOtpRepository;
		this.emailService = emailService;
        this.staffPasswordTokenRepository = staffPasswordTokenRepository;
        this.jwtUtil = new JwtUtil();
	}

	@Value("${strapi.url}")
	private String strapiUrl;

	@Value("${strapi.api.token}")
	private String strapiApiToken;

	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public StaffResponse createStaff(StaffCreateRequest request) {

		if (staffRepository.existsByEmailId(request.getEmailId())) {
			throw new DuplicateValuesException("Email already exists");
		}
		Staff staff = staffMapper.toEntity(request);
		staff.setStaffId(generateStaffId());

		// Upload profile img to Strapi
		if (request.getProfileImg() != null && !request.getProfileImg().isEmpty()) {
			String imageUrl = uploadToStrapi(request.getProfileImg());
			staff.setProfileImg(imageUrl);
		}

		staff.setPassword(null);
		staff.setEnabled("N");
		staff.setStatus("IN_ACTIVE");
		staff.setCreatedDt(LocalDateTime.now());

		Set<Role> roles = request.getRoleIds().stream()
				.map(roleRepository::findById)
				.map(role -> role.orElseThrow(() ->
						new ResourceNotFoundException("Role not found")))
				.collect(Collectors.toSet());

		staff.setRoles(roles);
		Staff savedStaff = staffRepository.save(staff);
		StaffPasswordToken passwordToken = new StaffPasswordToken();

		passwordToken.setToken(UUID.randomUUID().toString());
		passwordToken.setStaff(savedStaff);
		passwordToken.setExpiryTime(LocalDateTime.now().plusHours(24));
		passwordToken.setUsed(false);

		staffPasswordTokenRepository.save(passwordToken);

		emailService.sendStaffPasswordSetupMail(
				savedStaff.getEmailId(),
				savedStaff.getFirstNm(),
				passwordToken.getToken());
		return staffMapper.toResponse(savedStaff);
	}

	private String generateStaffId() {
		Long count = staffRepository.count() + 1;
		return String.format("SF%05d", count);
	}

	private String uploadToStrapi(MultipartFile file) {
		try {
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
			file.transferTo(tempFile);
			try {
				MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
				body.add("files", new FileSystemResource(tempFile));

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + strapiApiToken);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);

				ResponseEntity<String> response = restTemplate.exchange(strapiUrl + "/api/upload", HttpMethod.POST,
						new HttpEntity<>(body, headers), String.class);

				JsonNode root = new ObjectMapper().readTree(response.getBody());
				JsonNode fileNode = root.get(0);

				if (fileNode == null) {
					throw new RuntimeException("Invalid Strapi upload response: " + response.getBody());
				}

				String fileUrl = strapiUrl + fileNode.get("url").asText();
				logger.info("Profile image uploaded to Strapi: {}", fileUrl);
				return fileUrl;

			} finally {
				tempFile.delete();
			}
		} catch (Exception e) {
			logger.error("Failed to upload profile image to Strapi", e);
			throw new RuntimeException("Failed to upload profile image to Strapi", e);
		}
	}

	@Override
	public void setPassword(SetStaffPasswordRequest request) {

		StaffPasswordToken passwordToken = staffPasswordTokenRepository
				.findByToken(request.getToken())
				.orElseThrow(() -> new RuntimeException("Invalid password setup link"));

		if (passwordToken.getUsed()) {
			throw new RuntimeException(
					"Password link already used");
		}

		if (passwordToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Password link expired");
		}

		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new RuntimeException("Password and confirm password not matching");
		}

		Staff staff = passwordToken.getStaff();
		staff.setPassword(passwordEncoder.encode(request.getPassword()));
		staff.setEnabled("Y");
		staff.setStatus("ACTIVE");
		staffRepository.save(staff);

		// invalidate token
		passwordToken.setUsed(true);
		staffPasswordTokenRepository.save(passwordToken);
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
			return new ResourceNotFoundException("Staff not found");});

		logger.info("Staff fetched successfully for staffId: {}", staffId);
		return staffMapper.toResponse(staff);
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

		logger.info("OTP verification started for email: {}", request.getEmailId());

		// Fetch staff using email
		Staff staff = staffRepository.findByEmailId(request.getEmailId())
				.orElseThrow(() -> {
					logger.error("Staff not found for email: {}", request.getEmailId());
					return new ResourceNotFoundException("Staff not found");
				});

		// Fetch latest OTP using staffId
		StaffOtp otp = staffOtpRepository
				.findTopByStaffIdOrderByCreatedDtDesc(staff.getStaffId())
				.orElseThrow(() -> {
					logger.error("OTP not found for email: {}", request.getEmailId());
					return new OtpNotFoundException("OTP not found");
				});

		// CHECK OTP STATUS
		if (otp.getStatus() != OtpStatus.SENT) {
			logger.warn("Invalid OTP status for email: {}", request.getEmailId());
			throw new OtpInvalidException("OTP is not valid");
		}

		// CHECK OTP EXPIRY
		if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {
			otp.setStatus(OtpStatus.EXPIRED);
			otp.setUpdatedDt(LocalDateTime.now());
			staffOtpRepository.save(otp);

			logger.warn("OTP expired for email: {}", request.getEmailId());
			throw new OtpExpiredException("OTP expired");
		}

		// INVALID OTP
		if (!otp.getOtp().equals(request.getOtp())) {
			otp.setAttemptsNum(otp.getAttemptsNum() + 1);
			otp.setUpdatedDt(LocalDateTime.now());
			staffOtpRepository.save(otp);

			logger.warn("Invalid OTP entered for email: {}", request.getEmailId());
			throw new OtpInvalidException("Invalid OTP");
		}

		// GET ROLE
		String role = staff.getRoles().stream()
				.findFirst()
				.map(Role::getRoleNm)
				.orElse("STAFF");

		// GENERATE JWT TOKEN
		String token = jwtUtil.generateToken(
				staff.getEmailId(),
				role,
				staff.getStaffId()
		);

		// UPDATE OTP STATUS
		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());
		staffOtpRepository.save(otp);

		logger.info("OTP verified successfully for email: {}", request.getEmailId());

		// RESPONSE
		StaffLoginResponse response = new StaffLoginResponse();
		response.setStaffId(staff.getStaffId());
		response.setEmail(staff.getEmailId());
		response.setRole(role);
		response.setToken(token);
		response.setMessage("Login successful");

		return response;
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
	public Page<StaffResponse> getActiveStaff(int page, int size) {

		Pageable pageable = PageRequest.of(
				page,
				size,
				Sort.by("createdDt").descending());

		Page<Staff> staffPage = staffRepository.findByStatus("ACTIVE", pageable);

		return staffPage.map(staffMapper::toResponse);
	}

	@Override
	public Page<StaffResponse> getAllStaff(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDt").descending());

		Page<Staff> staffPage = staffRepository.findAll(pageable);

		return staffPage.map(staffMapper::toResponse);
	}

	@Override
	@Transactional
	public StaffResponse updateStaff(String staffId, StaffUpdateRequest request) {

		Staff staff = staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

		// Update basic details
		staff.setFirstNm(request.getFirstNm());
		staff.setLastNm(request.getLastNm());
		staff.setDob(request.getDob());
		staff.setGender(request.getGender());
		staff.setDateOfJoining(request.getDateOfJoining());

		// Update profile image
		if (request.getProfileImg() != null && !request.getProfileImg().isEmpty()) {

			String profileUrl = uploadToStrapi(request.getProfileImg());

			staff.setProfileImg(profileUrl);
		}

		// Update roles
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {

			Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));

			if (roles.size() != request.getRoleIds().size()) {
				throw new ResourceNotFoundException("One or more roles not found");
			}

			staff.setRoles(roles);
		}

		Staff updatedStaff = staffRepository.save(staff);

		return staffMapper.toResponse(updatedStaff);
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
			staff.setProfileImg(Arrays.toString(Base64.getDecoder().decode(base64)));
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