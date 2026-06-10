package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.AssignInstructorToCourseRequest;
import com.dmantz.lms.dto.response.InstructorResponse;
import com.dmantz.lms.dto.response.StaffCourseResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.StaffCourse;
import com.dmantz.lms.exceptions.CourseNotFoundException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StaffCourseMapper;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.StaffCourseRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.StaffCourseService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StaffCourseServiceImpl implements StaffCourseService {

	private static final Logger logger = LogManager.getLogger(StaffCourseServiceImpl.class);

	private final StaffCourseRepository staffCourseRepository;
	private final StaffRepository staffRepository;
	private final CourseRepository courseRepository;
	private final StaffCourseMapper staffCourseMapper;

	public StaffCourseServiceImpl(StaffCourseRepository staffCourseRepository, StaffRepository staffRepository,
			CourseRepository courseRepository, StaffCourseMapper staffCourseMapper) {
		this.staffCourseRepository = staffCourseRepository;
		this.staffRepository = staffRepository;
		this.courseRepository = courseRepository;
		this.staffCourseMapper = staffCourseMapper;
	}

	@Override
	public void assignInstructorsToCourse(String courseId, AssignInstructorToCourseRequest request) {

		logger.info("Assigning {} instructor(s) to courseId: {}", request.getStaffIds().size(), courseId);

		// 1. Verify course exists
		Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> {
			logger.warn("Course not found: {}", courseId);
			return new CourseNotFoundException("Course not found: " + courseId);
		});

		for (String staffId : request.getStaffIds()) {

			// 2. Verify staff exists
			Staff staff = staffRepository.findByStaffId(staffId).orElseThrow(() -> {
				logger.warn("Staff not found: {}", staffId);
				return new ResourceNotFoundException("Staff not found: " + staffId);
			});

			// 3. Check INSTRUCTOR role
			boolean isInstructor = staff.getRoles().stream().anyMatch(r -> r.getRoleNm().equalsIgnoreCase("INSTRUCTOR"));

			if (!isInstructor) {
				logger.warn("StaffId: {} does not have INSTRUCTOR role", staffId);
				throw new RuntimeException("Staff " + staffId + " is not an INSTRUCTOR");
			}

			// 4. Check duplicate
			boolean exists = staffCourseRepository.existsByStaff_StaffIdAndCourse_CourseId(staffId, courseId);

			if (exists) {
				logger.warn("StaffId: {} already assigned to courseId: {}", staffId, courseId);
				continue;
			}

			// 5. Save
			StaffCourse staffCourse = new StaffCourse();
			staffCourse.setStaff(staff);
			staffCourse.setCourse(course);
			staffCourseRepository.save(staffCourse);

			logger.info("StaffId: {} assigned to courseId: {}", staffId, courseId);
		}

		logger.info("Assignment completed for courseId: {}", courseId);
	}

	@Override
	public List<InstructorResponse> getInstructorsByCourse(String courseId) {

		logger.info("Fetching instructors for courseId: {}", courseId);

		// 1. Verify course exists
		courseRepository.findByCourseId(courseId).orElseThrow(() -> {
			logger.warn("Course not found: {}", courseId);
			return new CourseNotFoundException("Course not found: " + courseId);
		});

		// 2. Get mappings
		List<StaffCourse> staffCourses = staffCourseRepository.findByCourse_CourseId(courseId);

		if (staffCourses.isEmpty()) {
			logger.warn("No instructors found for courseId: {}", courseId);
			return List.of();
		}

		// 3. Filter INSTRUCTOR role only + map to chip response
		List<InstructorResponse> response = staffCourses.stream().map(StaffCourse::getStaff)
				.filter(staff -> staff.getRoles().stream().anyMatch(r -> r.getRoleNm().equalsIgnoreCase("INSTRUCTOR")))
				.map(staffCourseMapper::toChipResponse).toList();

		logger.info("Returning {} instructor chip(s) for courseId: {}", response.size(), courseId);

		return response;
	}

	@Override
	public void removeInstructorFromCourse(String courseId, String staffId) {

		logger.info("Removing staffId: {} from courseId: {}", staffId, courseId);

		StaffCourse staffCourse = staffCourseRepository.findByStaff_StaffIdAndCourse_CourseId(staffId, courseId)
				.orElseThrow(() -> {
					logger.warn("Mapping not found staffId: {} courseId: {}", staffId, courseId);
					return new ResourceNotFoundException(
							"Mapping not found for staffId: " + staffId + " courseId: " + courseId);
				});

		staffCourseRepository.delete(staffCourse);

		logger.info("StaffId: {} removed from courseId: {}", staffId, courseId);
	}

	@Override
	public List<StaffCourseResponse> getCoursesByStaff(String staffId) {

		logger.info("Fetching courses for staffId: {}", staffId);

		// 1. Verify staff exists
		staffRepository.findByStaffId(staffId).orElseThrow(() -> {
			logger.warn("Staff not found: {}", staffId);
			return new ResourceNotFoundException("Staff not found: " + staffId);
		});

		// 2. Get mappings
		List<StaffCourse> staffCourses = staffCourseRepository.findByStaff_StaffId(staffId);

		if (staffCourses.isEmpty()) {
			logger.warn("No courses found for staffId: {}", staffId);
			return List.of();
		}

		// 3. Map to response
		List<StaffCourseResponse> response = staffCourseMapper.toResponseList(staffCourses);

		logger.info("Returning {} course(s) for staffId: {}", response.size(), staffId);

		return response;
	}
}