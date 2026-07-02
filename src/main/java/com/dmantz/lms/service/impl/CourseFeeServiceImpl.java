package com.dmantz.lms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.CourseFeeRequest;
import com.dmantz.lms.dto.response.CourseFeeHistoryResponse;
import com.dmantz.lms.dto.response.CourseFeeSettingResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.CourseFee;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.CourseFeeMapper;
import com.dmantz.lms.repository.CourseFeeRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.CourseFeeService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseFeeServiceImpl implements CourseFeeService {

	private static final Logger logger = LogManager.getLogger(CourseFeeServiceImpl.class);

	private final CourseRepository courseRepository;
	private final CourseFeeRepository courseFeeRepository;
	private final StaffRepository staffRepository;
	private final CourseFeeMapper courseFeeMapper;

	public CourseFeeServiceImpl(CourseRepository courseRepository, CourseFeeRepository courseFeeRepository,
			StaffRepository staffRepository, CourseFeeMapper courseFeeMapper) {
		this.courseRepository = courseRepository;
		this.courseFeeRepository = courseFeeRepository;
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
		
		

		logger.info("Successfully fetched course fee setting for courseId: {}", courseId);

		return courseFeeMapper.toSettingResponse(course, feeRecords);
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
			logger.warn("Fee already exists for effective date: {} and courseId: {}", request.getEffectiveDate(), courseId);
			throw new DuplicateValuesException("Fee already exists for effective date: " + request.getEffectiveDate());
		}

		// Create Course Fee
		CourseFee courseFee = new CourseFee();
		courseFee.setCourse(course);
		courseFee.setCourseDuration(request.getCourseDuration()); // add this
		courseFee.setEffectiveDate(request.getEffectiveDate());
		courseFee.setFee(request.getFee());
		courseFee.setDiscount(discount);

		CourseFee savedFee = courseFeeRepository.save(courseFee);

		logger.info("Course fee created successfully for courseId: {} with feeId: {}", courseId, savedFee.getId());

		CourseFeeHistoryResponse response = courseFeeMapper.toHistoryResponse(savedFee);
		response.setSerialNumber(1); // always 1 since create only allows first fee
		return response;
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
			logger.warn("Fee already exists for effective date: {} and courseId: {}", request.getEffectiveDate(), courseId);
			throw new DuplicateValuesException("Fee already exists for effective date: " + request.getEffectiveDate());
		}

		// Create new fee version
		CourseFee courseFee = new CourseFee();
		courseFee.setCourse(course);
		courseFee.setCourseDuration(request.getCourseDuration()); 
		courseFee.setEffectiveDate(request.getEffectiveDate());
		courseFee.setFee(request.getFee());
		courseFee.setDiscount(discount);

		courseFeeRepository.save(courseFee);

		logger.info("New course fee version added successfully for courseId: {}", courseId);

		List<CourseFee> feeRecords = courseFeeRepository.findByCourse_IdOrderByEffectiveDateAsc(course.getId());

		logger.info("Course fee updated successfully for courseId: {}", courseId);

		return courseFeeMapper.toSettingResponse(course, feeRecords);
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
	        CourseFeeHistoryResponse h = courseFeeMapper.toHistoryResponse(feeRecord);
	        h.setSerialNumber(serialNumber++);
	        history.add(h);
	    }

	    logger.info("Successfully fetched {} fee history record(s) for courseId: {}",
	            history.size(), courseId);

	    return history;
	}

}