package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.ResendOtpResponse;
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
	private final SmsServiceImpl smsService;

	public StaffServiceImpl(StaffRepository staffRepository, RoleRepository roleRepository, StaffMapper staffMapper,
			PasswordEncoder passwordEncoder, StaffOtpRepository staffOtpRepository, EmailService emailService,
			StaffPasswordTokenRepository staffPasswordTokenRepository, SmsServiceImpl smsService) {

		this.staffRepository = staffRepository;
		this.roleRepository = roleRepository;
		this.staffMapper = staffMapper;
		this.passwordEncoder = passwordEncoder;
		this.staffOtpRepository = staffOtpRepository;
		this.emailService = emailService;
		this.staffPasswordTokenRepository = staffPasswordTokenRepository;
		this.jwtUtil = new JwtUtil();
		this.smsService = smsService;
	}

	@Value("${strapi.url}")
	private String strapiUrl;

	@Value("${strapi.api.token}")
	private String strapiApiToken;

	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	@Transactional
	public StaffResponse createStaff(StaffCreateRequest request) {

		logger.info("Staff creation started for email: {}", request.getEmailId());

		// Check email already exists
		if (staffRepository.existsByEmailId(request.getEmailId())) {
			logger.warn("Staff creation failed - email already exists: {}", request.getEmailId());
			throw new DuplicateValuesException("Email already exists");
		}

		// Validate OTP channel
		OtpChannel channel = request.getOtpChannel();
		if (channel == null) {
			logger.warn("OTP channel not specified for staff email: {}", request.getEmailId());
			throw new InvalidOtpChannelException("OTP channel must be specified: EMAIL or MOBILE");
		}

		// Create Staff
		Staff staff = staffMapper.toEntity(request);
		staff.setStaffId(generateStaffId());

		// Upload profile image to Strapi
		if (request.getProfileImg() != null && !request.getProfileImg().isEmpty()) {

			logger.info("Uploading staff profile image to Strapi for email: {}", request.getEmailId());

			String imageUrl = uploadToStrapi(request.getProfileImg());
			staff.setProfileImg(imageUrl);
		}

		staff.setPassword(null);
		staff.setEnabled("N");
		staff.setStatus("IN_ACTIVE");
		staff.setCreatedDt(LocalDateTime.now());

		// Set roles
		Set<Role> roles = request.getRoleIds().stream().map(roleRepository::findById)
				.map(role -> role.orElseThrow(() -> new ResourceNotFoundException("Role not found")))
				.collect(Collectors.toSet());

		staff.setRoles(roles);

		// Save staff
		Staff savedStaff = staffRepository.save(staff);

		logger.info("Staff created successfully with staffId: {}", savedStaff.getStaffId());

		// Create password setup token
		StaffPasswordToken passwordToken = new StaffPasswordToken();

		passwordToken.setToken(UUID.randomUUID().toString());
		passwordToken.setStaff(savedStaff);
		passwordToken.setExpiryTime(LocalDateTime.now().plusHours(24));
		passwordToken.setUsed(false);

		staffPasswordTokenRepository.save(passwordToken);

		logger.info("Staff password setup token created for staffId: {}", savedStaff.getStaffId());

		// Generate OTP
		StaffOtp otp = generateStaffOtp(savedStaff.getStaffId());

		try {

			switch (channel) {

			case EMAIL:

				emailService.sendStaffPasswordSetupMail(savedStaff.getEmailId(), savedStaff.getFirstNm(),
						passwordToken.getToken());

				logger.info("Staff password setup email sent to: {}", savedStaff.getEmailId());

				break;

			case MOBILE:

				smsService.sendStaffPasswordSetupSms(savedStaff.getMobileNum(), savedStaff.getFirstNm(),
						passwordToken.getToken());

				logger.info("Staff password setup SMS sent to: {}", savedStaff.getMobileNum());

				break;

			default:
				throw new InvalidOtpChannelException("Invalid OTP channel: " + channel);
			}

			// Mark OTP as sent
			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());
			staffOtpRepository.save(otp);

			logger.info("Staff OTP status updated to SENT for staffId: {}", savedStaff.getStaffId());

		} catch (InvalidOtpChannelException ex) {

			otp.setStatus(OtpStatus.FAILED);
			otp.setUpdatedDt(LocalDateTime.now());
			staffOtpRepository.save(otp);

			logger.error("Invalid OTP channel during staff creation: {}", ex.getMessage());

			throw ex;

		} catch (Exception ex) {

			otp.setStatus(OtpStatus.FAILED);
			otp.setUpdatedDt(LocalDateTime.now());
			staffOtpRepository.save(otp);

			logger.error("Failed to send staff OTP via {} for staffId: {}", channel, savedStaff.getStaffId(), ex);

			throw new OtpSendingException("Failed to send OTP via " + channel + ": " + ex.getMessage(), ex);
		}

		// Existing password setup email
		// Send password setup email
		emailService.sendStaffPasswordSetupMail(savedStaff.getEmailId(), savedStaff.getFirstNm(),
				passwordToken.getToken());

		logger.info("Staff password setup email sent to: {}", savedStaff.getEmailId());

		// Send password setup SMS
		smsService.sendStaffPasswordSetupSms(savedStaff.getMobileNum(), savedStaff.getFirstNm(),
				passwordToken.getToken());

		logger.info("Staff password setup SMS sent to: {}", savedStaff.getMobileNum());

		logger.info("Staff creation completed successfully for staffId: {}", savedStaff.getStaffId());

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

		StaffPasswordToken passwordToken = staffPasswordTokenRepository.findByToken(request.getToken())
				.orElseThrow(() -> new RuntimeException("Invalid password setup link"));

		if (passwordToken.getUsed()) {
			throw new RuntimeException("Password link already used");
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
			return new ResourceNotFoundException("Staff not found");
		});

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
	public StaffLoginResponse verifyStaffOtp(OtpVerifyRequest request) {

		logger.info("OTP verification started for identifier: {}", request.getEmailIdOrMobileNo());

		String identifier = request.getEmailIdOrMobileNo();

		// Validate OTP channel
		if (request.getChannel() == null) {
			throw new InvalidOtpChannelException("OTP channel must be specified: EMAIL or MOBILE");
		}

		// Fetch staff using email or mobile
		Staff staff;

		if (request.getChannel() == OtpChannel.EMAIL) {

			staff = staffRepository.findByEmailId(identifier).orElseThrow(() -> {
				logger.error("Staff not found for email: {}", identifier);
				return new ResourceNotFoundException("Staff not found");
			});

		} else if (request.getChannel() == OtpChannel.MOBILE) {

			staff = (Staff) staffRepository.findByMobileNum(identifier).orElseThrow(() -> {
				logger.error("Staff not found for mobile: {}", identifier);
				return new ResourceNotFoundException("Staff not found");
			});

		} else {

			throw new InvalidOtpChannelException("Invalid OTP channel: " + request.getChannel());
		}

		// Fetch latest OTP using staffId
		StaffOtp otp = staffOtpRepository.findTopByStaffIdOrderByCreatedDtDesc(staff.getStaffId()).orElseThrow(() -> {
			logger.error("OTP not found for staff: {}", staff.getStaffId());

			return new OtpNotFoundException("OTP not found");
		});

		// CHECK OTP STATUS
		if (otp.getStatus() != OtpStatus.SENT) {

			logger.warn("Invalid OTP status for staffId: {}", staff.getStaffId());

			throw new OtpInvalidException("OTP is not valid");
		}

		// CHECK OTP EXPIRY
		if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {

			otp.setStatus(OtpStatus.EXPIRED);
			otp.setUpdatedDt(LocalDateTime.now());

			staffOtpRepository.save(otp);

			logger.warn("OTP expired for staffId: {}", staff.getStaffId());

			throw new OtpExpiredException("OTP expired");
		}

		// INVALID OTP
		if (!otp.getOtp().equals(request.getOtp())) {

			otp.setAttemptsNum(otp.getAttemptsNum() + 1);
			otp.setUpdatedDt(LocalDateTime.now());

			staffOtpRepository.save(otp);

			logger.warn("Invalid OTP entered for staffId: {}", staff.getStaffId());

			throw new OtpInvalidException("Invalid OTP");
		}

		// GET ROLE
		String role = staff.getRoles().stream().findFirst().map(Role::getRoleNm).orElse("STAFF");

		// GENERATE JWT TOKEN
		String token = jwtUtil.generateToken(staff.getEmailId(), role, staff.getStaffId());

		// UPDATE OTP STATUS
		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());

		staffOtpRepository.save(otp);

		logger.info("OTP verified successfully for staffId: {}", staff.getStaffId());

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
	@Transactional
	public StaffResponse updateStaff(
	        String staffId,
	        StaffUpdateRequest request) {

	    Staff staff = staffRepository.findByStaffId(staffId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Staff not found"));

	    // DTO → Entity
	    staffMapper.updateEntity(request, staff);

	    // Update roles
	    if (request.getRoles() != null && !request.getRoles().isEmpty()) {

	        Set<Role> roles = request.getRoles()
	                .stream()
	                .map(roleName -> roleRepository.findByRoleNm(roleName)
	                        .orElseThrow(() ->
	                                new ResourceNotFoundException(
	                                        "Role not found: " + roleName)))
	                .collect(Collectors.toSet());

	        staff.setRoles(roles);
	    }

	    Staff updatedStaff = staffRepository.save(staff);

	    // Entity → Response
	    return staffMapper.toResponse(updatedStaff);
	}
	@Override
	public StaffResponse updateProfileImage(String staffId, MultipartFile file) {

		Staff staff = staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

		if (file == null || file.isEmpty()) {
			throw new BadRequestException("Profile image is required.");
		}

		// Upload image to Strapi
		String imageUrl = uploadToStrapi(file);

		// Save image URL
		staff.setProfileImg(imageUrl);

		Staff savedStaff = staffRepository.save(staff);

		return staffMapper.toResponse(savedStaff);
	}

	@Override
	public void forgotPassword(ForgotPasswordRequest request) {

		String identifier = request.getEmailIdOrMobileNo();

		Staff staff;

		if (identifier.contains("@")) {

			// Search by email
			staff = staffRepository.findByEmailId(identifier)
					.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

		} else {

			// Search by mobile number
			staff = (Staff) staffRepository.findByMobileNum(identifier)
					.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
		}

		String token = UUID.randomUUID().toString();

		StaffPasswordToken passwordToken = new StaffPasswordToken();

		passwordToken.setToken(token);
		passwordToken.setStaff(staff);
		passwordToken.setExpiryTime(LocalDateTime.now().plusMinutes(30));
		passwordToken.setUsed(false);

		staffPasswordTokenRepository.save(passwordToken);

		String resetLink = "http://localhost:5173/reset-password?token=" + token;

		if (identifier.contains("@")) {

			// Send reset link through email
			emailService.sendResetPasswordEmail(staff.getEmailId(), staff.getFirstNm(), resetLink);

			logger.info("Password reset link sent through email to: {}", staff.getEmailId());

		} else {

			// Send reset link through SMS
			smsService.sendResetPasswordSms(staff.getMobileNum(), staff.getFirstNm(), resetLink);

			logger.info("Password reset link sent through SMS to: {}", staff.getMobileNum());
		}
	}

	@Override
	public void validateResetToken(String token) {

		StaffPasswordToken passwordToken = staffPasswordTokenRepository.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid reset token"));

		if (passwordToken.getUsed()) {
			throw new BadRequestException("Reset token has already been used.");
		}

		if (passwordToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("Reset token has expired.");
		}
	}

	@Override
	public void resetPassword(SetStaffPasswordRequest request) {

		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new BadRequestException("New Password and Confirm Password do not match.");
		}

		StaffPasswordToken passwordToken = staffPasswordTokenRepository.findByToken(request.getToken())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid reset token"));

		if (passwordToken.getUsed()) {
			throw new BadRequestException("Reset token has already been used.");
		}

		if (passwordToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("Reset token has expired.");
		}

		Staff staff = passwordToken.getStaff();

		staff.setPassword(passwordEncoder.encode(request.getPassword()));
		staff.setStatus("ACTIVE");
		staff.setEnabled("Y");

		staffRepository.save(staff);

		passwordToken.setUsed(true);
		staffPasswordTokenRepository.save(passwordToken);
	}

	@Override
	public ResendOtpResponse resendLoginOtp(ResendStaffOtpRequest request) {

	    String identifier = request.getEmailIdOrMobileNo();

	    logger.info("Resend OTP requested for: {}", identifier);

	    Staff staff;

	    // Find staff using email or mobile
	    if (identifier.contains("@")) {

	        staff = staffRepository.findByEmailId(identifier)
	                .orElseThrow(() -> {
	                    logger.warn("Staff not found with email: {}", identifier);
	                    return new RuntimeException("Staff not found");
	                });

	    } else {

	        staff = (Staff) staffRepository.findByMobileNum(identifier)
	                .orElseThrow(() -> {
	                    logger.warn("Staff not found with mobile: {}", identifier);
	                    return new RuntimeException("Staff not found");
	                });
	    }

	    if (!"Y".equals(staff.getEnabled())) {

	        logger.warn("Disabled account for staffId: {}", staff.getStaffId());

	        throw new RuntimeException("Account disabled");
	    }

	    // Expire previous login OTP
	    staffOtpRepository
	            .findTopByStaffIdOrderByCreatedDtDesc(staff.getStaffId())
	            .ifPresent(oldOtp -> {

	                oldOtp.setStatus(OtpStatus.EXPIRED);
	                oldOtp.setUpdatedDt(LocalDateTime.now());

	                staffOtpRepository.save(oldOtp);
	            });

	    // Generate new OTP
	    StaffOtp newOtp = generateStaffOtp(staff.getStaffId());

	    try {

	        if (identifier.contains("@")) {

	            // Send OTP through email
	            emailService.sendOtpEmail(
	                    staff.getEmailId(),
	                    newOtp.getOtp(),
	                    OtpPurpose.LOGIN
	            );

	            logger.info(
	                    "Login OTP sent successfully through email to staffId: {}",
	                    staff.getStaffId()
	            );

	        } else {

	            // Send OTP through SMS
	            smsService.sendOtpSms(
	                    staff.getMobileNum(),
	                    newOtp.getOtp(),
	                    OtpPurpose.STAFF_LOGIN
	            );

	            logger.info(
	                    "Staff login OTP sent successfully through SMS to staffId: {}",
	                    staff.getStaffId()
	            );
	        }

	        newOtp.setStatus(OtpStatus.SENT);
	        newOtp.setUpdatedDt(LocalDateTime.now());

	        staffOtpRepository.save(newOtp);

	    } catch (Exception e) {

	        logger.error(
	                "Failed to send OTP for staffId: {}",
	                staff.getStaffId(),
	                e
	        );

	        newOtp.setStatus(OtpStatus.FAILED);
	        newOtp.setUpdatedDt(LocalDateTime.now());

	        staffOtpRepository.save(newOtp);

	        throw new RuntimeException("Failed to send OTP");
	    }
	    ResendOtpResponse response = new ResendOtpResponse();

	    response.setStaffId(staff.getStaffId());

	    if (identifier.contains("@")) {
	        response.setEmail(staff.getEmailId());
	    } else {
	        response.setEmail(staff.getMobileNum());
	    }

	    response.setMessage("OTP resent successfully");

	    return response;
	}
	@Override
	public Page<StaffResponse> getActiveStaff(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDt").descending());

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

	private void deleteFromStrapiByUrl(String fileUrl) {
		try {
			String urlPath = fileUrl.replace(strapiUrl, "");

			String searchUrl = strapiUrl + "/api/upload/files?filters[url][$eq]=" + urlPath;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + strapiApiToken);

			ResponseEntity<String> searchResponse = restTemplate.exchange(searchUrl, HttpMethod.GET,
					new HttpEntity<>(headers), String.class);

			JsonNode root = new ObjectMapper().readTree(searchResponse.getBody());

			if (root == null || !root.isArray() || root.size() == 0) {
				logger.warn("Profile image not found in Strapi by URL: {}", urlPath);
				return;
			}

			Long strapiFileId = root.get(0).get("id").asLong();

			String deleteUrl = strapiUrl + "/api/upload/files/" + strapiFileId;

			restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

			logger.info("Staff profile image deleted from Strapi: {}", urlPath);

		} catch (Exception e) {
			logger.error("Failed to delete staff profile image from Strapi. URL: {}", fileUrl, e);

			throw new RuntimeException("Failed to delete profile image from Strapi", e);
		}
	}

	@Override
	@Transactional
	public void deleteProfileImage(String staffId) {

		logger.info("Deleting profile image for staffId: {}", staffId);

		Staff staff = staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found for staffId: " + staffId));

		String profileImg = staff.getProfileImg();

		if (profileImg == null || profileImg.isBlank()) {
			throw new BadRequestException("Staff profile image not found.");
		}

		// Delete actual image from Strapi
		deleteFromStrapiByUrl(profileImg);

		// Remove URL from database
		staff.setProfileImg(null);

		staffRepository.save(staff);

		logger.info("Staff profile image deleted successfully for staffId: {}", staffId);
	}
}