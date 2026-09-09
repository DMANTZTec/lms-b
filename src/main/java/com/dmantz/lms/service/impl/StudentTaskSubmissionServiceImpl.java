package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentTaskSubmissionRequest;
import com.dmantz.lms.dto.response.StudentTaskSubmissionResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTaskSubmissionMapper;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.StudentTaskSubmissionService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentTaskSubmissionServiceImpl implements StudentTaskSubmissionService {

	private static final Logger logger = LogManager.getLogger(StudentTaskSubmissionServiceImpl.class);

	@Value("${strapi.url}")
	private String strapiUrl;

	@Value("${strapi.api.token}")
	private String strapiApiToken;

	private final RestTemplate restTemplate = new RestTemplate();

	private final StudentTaskSubmissionRepository submissionRepository;
	private final StudentTaskRepository studentTaskRepository;
	private final StudentRepository studentRepository;
	private final StaffCourseRepository staffCourseRepository;
	private final StudentTaskSubmissionMapper submissionMapper;

	public StudentTaskSubmissionServiceImpl(StudentTaskSubmissionRepository submissionRepository,
			StudentTaskRepository studentTaskRepository, StudentRepository studentRepository,
			StaffCourseRepository staffCourseRepository, StudentTaskSubmissionMapper submissionMapper) {

		this.submissionRepository = submissionRepository;
		this.studentTaskRepository = studentTaskRepository;
		this.studentRepository = studentRepository;
		this.staffCourseRepository = staffCourseRepository;
		this.submissionMapper = submissionMapper;
	}

	@Override
	public StudentTaskSubmissionResponse submitTask(StudentTaskSubmissionRequest request) throws Exception {

		logger.info("Submitting task for studentId: {} taskId: {}", request.getStudentId(), request.getStudentTaskId());

		Student student = studentRepository.findByStudentId(request.getStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

		StudentTask task = studentTaskRepository.findById(request.getStudentTaskId())
				.orElseThrow(() -> new ResourceNotFoundException("Task not found: " + request.getStudentTaskId()));

		if (!task.getStudent().getStudentId().equals(request.getStudentId())) {
			throw new IllegalStateException("This task does not belong to the given student");
		}

		List<StaffCourse> staffCourses = staffCourseRepository.findByCourse_CourseId(task.getCourse().getCourseId());

		if (staffCourses.isEmpty()) {
			throw new ResourceNotFoundException("No instructors assigned to course: " + task.getCourse().getCourseId());
		}

		if (staffCourses.size() > 1) {
			logger.warn("Course {} has {} instructors assigned; defaulting to the first for this submission's reviewer",
					task.getCourse().getCourseId(), staffCourses.size());
		}

		Staff instructor = staffCourses.get(0).getStaff();

		// ===== Upload attachments to Strapi (multi-file) =====
		List<FileAttachment> uploadedAttachments = uploadAttachments(request.getAttachments());

		StudentTaskSubmission submission = submissionMapper.toEntity(request, task, student, instructor);
		submission.setAttachments(uploadedAttachments);

		StudentTaskSubmission saved = submissionRepository.save(submission);

		task.setStatus(StudentTaskStatus.COMPLETED);
		studentTaskRepository.save(task);

		logger.info("Task submission {} is pending review from instructor: {}", saved.getId(), instructor.getStaffId());

		logger.info("Task submitted successfully with submissionId: {}", saved.getId());

		return submissionMapper.toResponse(saved);
	}

	// ================= UPLOAD ALL ATTACHMENTS =================
	private List<FileAttachment> uploadAttachments(List<MultipartFile> files) throws Exception {

		List<FileAttachment> attachments = new ArrayList<>();

		for (MultipartFile file : files) {

			if (file == null || file.isEmpty()) {
				continue;
			}

			logger.info("Uploading submission attachment: {}", file.getOriginalFilename());

			JsonNode fileNode = uploadToStrapi(file);

			String fileUrl = strapiUrl + fileNode.get("url").asText();
			String fileName = fileNode.has("name") ? fileNode.get("name").asText() : file.getOriginalFilename();

			attachments.add(new FileAttachment(fileName, fileUrl, file.getContentType()));

			logger.info("Attachment uploaded: {}", fileUrl);
		}

		if (attachments.isEmpty()) {
			throw new IllegalArgumentException("At least one valid attachment is required");
		}

		return attachments;
	}

	// ================= STRAPI HELPER — build auth headers =================
	private HttpHeaders buildStrapiAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + strapiApiToken);
		return headers;
	}

	// ================= UPLOAD to Strapi =================
	private JsonNode uploadToStrapi(MultipartFile file) throws Exception {

		File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
		file.transferTo(tempFile);

		try {
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("files", new FileSystemResource(tempFile));

			HttpHeaders headers = buildStrapiAuthHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			ResponseEntity<String> response = restTemplate.exchange(strapiUrl + "/api/upload", HttpMethod.POST,
					new HttpEntity<>(body, headers), String.class);

			logger.info("Strapi upload status: {}", response.getStatusCode());
			logger.info("Strapi upload body: {}", response.getBody());

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			JsonNode fileNode = root.get(0);

			if (fileNode == null) {
				throw new RuntimeException("Invalid Strapi upload response: " + response.getBody());
			}

			return fileNode;

		} finally {
			tempFile.delete();
		}
	}
}