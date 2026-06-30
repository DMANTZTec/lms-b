package com.dmantz.lms.service.impl;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.CourseFeeRequest;
import com.dmantz.lms.dto.response.CourseFeeHistoryResponse;
import com.dmantz.lms.dto.response.CourseFeeSettingResponse;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.CourseFee;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.CourseFeeMapper;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.CourseFeeRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.CourseFeeService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseFeeServiceImpl implements CourseFeeService {

	private static final Logger logger = LogManager.getLogger(CourseFeeServiceImpl.class);
	private static final String ACTIVE_BATCH_STATUS = "ACTIVE";

	private final CourseRepository courseRepository;
	private final CourseFeeRepository courseFeeRepository;
	private final ClassBatchRepository classBatchRepository;
	private final StaffRepository staffRepository;
	private final CourseFeeMapper courseFeeMapper;

	public CourseFeeServiceImpl(CourseRepository courseRepository, CourseFeeRepository courseFeeRepository,
			ClassBatchRepository classBatchRepository, StaffRepository staffRepository,
			CourseFeeMapper courseFeeMapper) {
		this.courseRepository = courseRepository;
		this.courseFeeRepository = courseFeeRepository;
		this.classBatchRepository = classBatchRepository;
		this.staffRepository = staffRepository;
		this.courseFeeMapper = courseFeeMapper;
	}

	@Override
	public CourseFeeSettingResponse getCourseFeeSetting(String courseId) {
		logger.info("Fetching course fee setting for courseId: {}", courseId);

		Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> {
			logger.error("Course not found with courseId: {}", courseId);
			return new ResourceNotFoundException("Course not found with courseId: " + courseId);
		});

		List<CourseFee> feeRecords = courseFeeRepository.findByCourse_IdOrderByEffectiveDateAsc(course.getId());

		List<CourseFeeHistoryResponse> history = new ArrayList<>();
		int serialNumber = 1;

		for (CourseFee feeRecord : feeRecords) {
			history.add(courseFeeMapper.toHistoryResponse(feeRecord, serialNumber++));
		}

		CourseFeeSettingResponse response = new CourseFeeSettingResponse();
		response.setCourseId(course.getCourseId());
		response.setCourseTitle(course.getCourseTitle());
		response.setSubjectNm(course.getSubject() != null ? course.getSubject().getSubjectNm() : null);

		ClassBatch batch = classBatchRepository
				.findTopByCourse_CourseIdAndStatusOrderByStartDateDesc(courseId, ACTIVE_BATCH_STATUS).orElseGet(
						() -> classBatchRepository.findTopByCourse_CourseIdOrderByStartDateDesc(courseId).orElse(null));

		if (batch == null) {
			logger.warn("No batch found for courseId: {}", courseId);

			response.setCourseDuration("—");
			response.setBatchClassName(null);
			response.setBatchStatus(null);
		} else {

			long weeks = ChronoUnit.WEEKS.between(batch.getStartDate(), batch.getEndDate());

			if (weeks < 1) {
				weeks = 1;
			}

			response.setCourseDuration(weeks + " Weeks");
			response.setBatchClassName(batch.getClassName());
			response.setBatchStatus(batch.getStatus());
		}

		response.setFeeHistory(history);
		response.setTotalHistoryRecords(history.size());
		response.setCurrentFee(history.isEmpty() ? null : history.get(history.size() - 1));

		logger.info("Successfully fetched course fee setting for courseId: {}", courseId);

		return response;
	}

	@Override
	public CourseFeeHistoryResponse createCourseFee(String courseId, CourseFeeRequest request, String staffId) {

		logger.info("Creating initial course fee for courseId: {} by staffId: {}", courseId, staffId);

		// Validate Staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.error("Staff not found with staffId: {}", staffId);
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Validate Course
		Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> {
			logger.error("Course not found with courseId: {}", courseId);
			return new ResourceNotFoundException("Course not found with courseId: " + courseId);
		});

		// Validate Discount
		BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;

		if (discount.compareTo(request.getFee()) > 0) {
			logger.error("Discount {} is greater than fee {} for courseId: {}", discount, request.getFee(), courseId);
			throw new IllegalArgumentException("Discount cannot be greater than fee");
		}

		// Check if fee already exists
		if (courseFeeRepository.findTopByCourse_IdOrderByEffectiveDateDesc(course.getId()).isPresent()) {
			logger.warn("Course fee already exists for courseId: {}", courseId);
			throw new DuplicateValuesException("Course fee already exists. Use update API to add a new fee version.");
		}

		// Check duplicate effective date
		if (courseFeeRepository.existsByCourse_IdAndEffectiveDate(course.getId(), request.getEffectiveDate())) {

			logger.warn("Fee already exists for effective date: {} and courseId: {}", request.getEffectiveDate(),
					courseId);

			throw new DuplicateValuesException("Fee already exists for effective date: " + request.getEffectiveDate());
		}

		// Create Course Fee
		CourseFee courseFee = new CourseFee();
		courseFee.setCourse(course);
		courseFee.setEffectiveDate(request.getEffectiveDate());
		courseFee.setFee(request.getFee());
		courseFee.setDiscount(discount);

		CourseFee savedFee = courseFeeRepository.save(courseFee);

		logger.info("Course fee created successfully for courseId: {} with feeId: {}", courseId, savedFee.getId());

		return courseFeeMapper.toHistoryResponse(savedFee, 1);
	}

	@Override
	public CourseFeeSettingResponse updateCourseFee(String courseId, CourseFeeRequest request, String staffId) {

		logger.info("Updating course fee for courseId: {} by staffId: {}", courseId, staffId);

		// Validate Staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.error("Staff not found with staffId: {}", staffId);
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Validate Course
		Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> {
			logger.error("Course not found with courseId: {}", courseId);
			return new ResourceNotFoundException("Course not found with courseId: " + courseId);
		});

		// Validate Discount
		BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;

		if (discount.compareTo(request.getFee()) > 0) {
			logger.error("Discount {} is greater than fee {} for courseId: {}", discount, request.getFee(), courseId);
			throw new IllegalArgumentException("Discount cannot be greater than fee");
		}

		// Check duplicate effective date
		if (courseFeeRepository.existsByCourse_IdAndEffectiveDate(course.getId(), request.getEffectiveDate())) {

			logger.warn("Fee already exists for effective date: {} and courseId: {}", request.getEffectiveDate(),
					courseId);

			throw new DuplicateValuesException("Fee already exists for effective date: " + request.getEffectiveDate());
		}

		// Create new fee version
		CourseFee courseFee = new CourseFee();
		courseFee.setCourse(course);
		courseFee.setEffectiveDate(request.getEffectiveDate());
		courseFee.setFee(request.getFee());
		courseFee.setDiscount(discount);

		courseFeeRepository.save(courseFee);

		logger.info("New course fee version added successfully for courseId: {}", courseId);

		// Fetch updated fee setting
		logger.info("Fetching updated course fee setting for courseId: {}", courseId);

		CourseFeeSettingResponse response = new CourseFeeSettingResponse();

		List<CourseFee> feeRecords = courseFeeRepository.findByCourse_IdOrderByEffectiveDateAsc(course.getId());

		List<CourseFeeHistoryResponse> history = new ArrayList<>();
		int serialNumber = 1;

		for (CourseFee feeRecord : feeRecords) {
			history.add(courseFeeMapper.toHistoryResponse(feeRecord, serialNumber++));
		}

		response.setCourseId(course.getCourseId());
		response.setCourseTitle(course.getCourseTitle());
		response.setSubjectNm(course.getSubject() != null ? course.getSubject().getSubjectNm() : null);

		ClassBatch batch = classBatchRepository
				.findTopByCourse_CourseIdAndStatusOrderByStartDateDesc(courseId, ACTIVE_BATCH_STATUS).orElseGet(
						() -> classBatchRepository.findTopByCourse_CourseIdOrderByStartDateDesc(courseId).orElse(null));

		if (batch == null) {
			logger.warn("No batch found for courseId: {}", courseId);

			response.setCourseDuration("—");
			response.setBatchClassName(null);
			response.setBatchStatus(null);
		} else {

			long weeks = ChronoUnit.WEEKS.between(batch.getStartDate(), batch.getEndDate());

			if (weeks < 1) {
				weeks = 1;
			}

			response.setCourseDuration(weeks + " Weeks");
			response.setBatchClassName(batch.getClassName());
			response.setBatchStatus(batch.getStatus());
		}

		response.setFeeHistory(history);
		response.setTotalHistoryRecords(history.size());
		response.setCurrentFee(history.isEmpty() ? null : history.get(history.size() - 1));

		logger.info("Course fee updated successfully for courseId: {}", courseId);

		return response;
	}

	@Override
	public List<CourseFeeHistoryResponse> getFeeHistory(String courseId) {

	    logger.info("Fetching fee history for courseId: {}", courseId);

	    // Validate Course
	    Course course = courseRepository.findByCourseId(courseId)
	            .orElseThrow(() -> {
	                logger.error("Course not found with courseId: {}", courseId);
	                return new ResourceNotFoundException("Course not found with courseId: " + courseId);
	            });

	    // Fetch Fee Records
	    List<CourseFee> feeRecords = courseFeeRepository
	            .findByCourse_IdOrderByEffectiveDateAsc(course.getId());

	    if (feeRecords.isEmpty()) {
	        logger.warn("No fee history found for courseId: {}", courseId);
	        return Collections.emptyList();
	    }

	    // Build Response
	    List<CourseFeeHistoryResponse> history = new ArrayList<>();
	    int serialNumber = 1;

	    for (CourseFee feeRecord : feeRecords) {
	        history.add(courseFeeMapper.toHistoryResponse(feeRecord, serialNumber++));
	    }

	    logger.info("Successfully fetched {} fee history record(s) for courseId: {}",
	            history.size(), courseId);

	    return history;
	}

}