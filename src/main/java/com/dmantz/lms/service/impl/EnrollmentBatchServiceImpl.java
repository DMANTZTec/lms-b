package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.AssignStudentToBatchRequest;
import com.dmantz.lms.dto.response.DailyScheduleResponse;
import com.dmantz.lms.dto.response.EnrollmentBatchResponse;
import com.dmantz.lms.dto.response.ScheduleItemResponse;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentBatch;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.mapper.EnrollmentBatchMapper;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.ClassScheduleRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.EnrollmentBatchService;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentBatchServiceImpl implements EnrollmentBatchService {

	private final EnrollmentBatchRepository enrollmentBatchRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final ClassBatchRepository classBatchRepository;
	private final ClassScheduleRepository classScheduleRepository;
	private final StaffRepository staffRepository;
	private final EnrollmentBatchMapper mapper;

	public EnrollmentBatchServiceImpl(EnrollmentBatchRepository enrollmentBatchRepository,
			EnrollmentRepository enrollmentRepository, ClassBatchRepository classBatchRepository,
			ClassScheduleRepository classScheduleRepository, StaffRepository staffRepository,
			EnrollmentBatchMapper mapper) {

		this.enrollmentBatchRepository = enrollmentBatchRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.classBatchRepository = classBatchRepository;
		this.classScheduleRepository = classScheduleRepository;
		this.staffRepository = staffRepository;
		this.mapper = mapper;
	}

	// =========================================================
	// ASSIGN ENROLLED STUDENT TO BATCH
	// =========================================================

	@Override
	public EnrollmentBatchResponse assignStudentToBatch(AssignStudentToBatchRequest request) {

		/*
		 * 1. Find enrollment
		 */
		Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
				.orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + request.getEnrollmentId()));

		/*
		 * 2. Find batch
		 */
		ClassBatch batch = classBatchRepository.findById(request.getBatchId())
				.orElseThrow(() -> new RuntimeException("Batch not found with id: " + request.getBatchId()));

		/*
		 * 4. Validate that student's enrollment belongs to the course of this batch.
		 */
		validateEnrollmentForBatch(enrollment, batch);

		/*
		 * 5. Prevent duplicate assignment
		 */
		if (enrollmentBatchRepository.existsByEnrollmentIdAndClassBatchId(enrollment.getId(), batch.getId())) {

			throw new RuntimeException("Student is already assigned to this batch");
		}

		/*
		 * 6. Check batch capacity
		 */
		if (batch.getCapacity() != null) {

			long currentStudentCount = enrollmentBatchRepository.countByClassBatchId(batch.getId());

			if (currentStudentCount >= batch.getCapacity()) {

				throw new RuntimeException("Batch capacity is full");
			}
		}

		/*
		 * 7. Get staff from existing authentication
		 *
		 * Request does NOT contain staffId.
		 */
		Staff authenticatedStaff = getAuthenticatedStaff();

		/*
		 * 8. Create EnrollmentBatch
		 */
		EnrollmentBatch enrollmentBatch = new EnrollmentBatch();

		enrollmentBatch.setEnrollment(enrollment);

		enrollmentBatch.setClassBatch(batch);

		enrollmentBatch.setAssignedBy(authenticatedStaff);

		enrollmentBatch.setAssignedDate(LocalDateTime.now());

		/*
		 * 9. Save
		 */
		EnrollmentBatch saved = enrollmentBatchRepository.save(enrollmentBatch);

		/*
		 * 10. Convert entity → response using MapStruct
		 */
		return mapper.toResponse(saved);
	}

	// =========================================================
	// VALIDATE ENROLLMENT COURSE
	// =========================================================

	private void validateEnrollmentForBatch(Enrollment enrollment, ClassBatch batch) {

		if (batch.getCourse() == null) {

			throw new RuntimeException("Batch is not associated with a course");
		}

		String batchCourseId = batch.getCourse().getCourseId();

		/*
		 * CASE 1: Student directly enrolled in a course.
		 *
		 * Enrollment ↓ Course
		 */
		if (enrollment.getCourse() != null) {

			String enrolledCourseId = enrollment.getCourse().getCourseId();

			if (!batchCourseId.equals(enrolledCourseId)) {

				throw new RuntimeException("Student is not enrolled in the course " + "of this batch");
			}

			return;
		}

		/*
		 * CASE 2: Student enrolled through a program.
		 *
		 * Enrollment ↓ Program ↓ Courses
		 */
		if (enrollment.getProgram() != null) {

			boolean courseExists = enrollment.getProgram().getProgramCourses().stream()
					.anyMatch(course -> batchCourseId.equals(course.getCourse()));

			if (!courseExists) {

				throw new RuntimeException("Batch course does not belong " + "to student's enrolled program");
			}

			return;
		}

		throw new RuntimeException("Enrollment has neither course nor program");
	}

	// =========================================================
	// GET ALL STUDENTS IN A BATCH
	// =========================================================

	@Override
	public List<EnrollmentBatchResponse> getStudentsByBatch(Long batchId) {

		/*
		 * Make sure batch exists
		 */
		classBatchRepository.findById(batchId)
				.orElseThrow(() -> new RuntimeException("Batch not found with id: " + batchId));

		/*
		 * Get all students assigned to batch
		 */
		return enrollmentBatchRepository.findByClassBatchId(batchId).stream().map(mapper::toResponse).toList();
	}

	// =========================================================
	// GET ENROLLMENT-BATCH BY ID
	// =========================================================

	@Override
	public EnrollmentBatchResponse getEnrollmentBatch(Long enrollmentBatchId) {

		EnrollmentBatch enrollmentBatch = enrollmentBatchRepository.findById(enrollmentBatchId)
				.orElseThrow(() -> new RuntimeException("EnrollmentBatch not found with id: " + enrollmentBatchId));

		return mapper.toResponse(enrollmentBatch);
	}

	// =========================================================
	// REMOVE STUDENT FROM BATCH
	// =========================================================

	@Override
	public void removeStudentFromBatch(Long enrollmentBatchId) {

		EnrollmentBatch enrollmentBatch = enrollmentBatchRepository.findById(enrollmentBatchId)
				.orElseThrow(() -> new RuntimeException("EnrollmentBatch not found with id: " + enrollmentBatchId));

		enrollmentBatchRepository.delete(enrollmentBatch);
	}

	// =========================================================
	// GET STUDENT WEEKLY SCHEDULE
	// =========================================================
	@Override
	public List<DailyScheduleResponse> getStudentWeeklySchedule(String studentId) {

		/*
		 * 1. Get student's batch assignments
		 */
		List<EnrollmentBatch> assignments = enrollmentBatchRepository.findByEnrollmentStudentStudentId(studentId);

		if (assignments.isEmpty()) {
			return List.of();
		}

		/*
		 * 2. Extract batch IDs
		 */
		List<Long> batchIds = assignments.stream().map(assignment -> assignment.getClassBatch().getId()).distinct()
				.toList();

		/*
		 * 3. Calculate current week range
		 *
		 * Monday -> Sunday
		 */
		LocalDate today = LocalDate.now();

		LocalDate startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

		LocalDate endDate = startDate.plusDays(6);

		/*
		 * 4. Fetch schedules for current week
		 */
		List<ClassSchedule> schedules = classScheduleRepository
				.findByClassBatchIdInAndClassDateBetweenOrderByClassDateAscStartTimeAsc(batchIds, startDate, endDate);
		/*
		 * 5. Group schedules by day
		 */
		Map<String, List<ClassSchedule>> schedulesByDay = new LinkedHashMap<>();

		for (ClassSchedule schedule : schedules) {

			String day = schedule.getClassDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

			schedulesByDay.computeIfAbsent(day, key -> new ArrayList<>()).add(schedule);
		}

		/*
		 * 6. Convert grouped schedules to response
		 */
		return schedulesByDay.entrySet().stream().map(entry -> {

			DailyScheduleResponse response = new DailyScheduleResponse();

			response.setDay(entry.getKey());

			List<ScheduleItemResponse> items = entry.getValue().stream().map(this::convertToScheduleItem).toList();

			response.setItems(items);

			return response;
		}).toList();
	}

	private ScheduleItemResponse convertToScheduleItem(ClassSchedule schedule) {

		ScheduleItemResponse item = new ScheduleItemResponse();

		/*
		 * Course / Class title
		 */
		item.setTitle(schedule.getClassBatch().getCourse().getCourseTitle());

		/*
		 * Time formatting
		 */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

		String time = schedule.getStartTime().format(formatter) + " - " + schedule.getEndTime().format(formatter);

		item.setTime(time);

		/*
		 * Instructor
		 */
		if (schedule.getStaff() != null) {

			item.setInstructor(schedule.getStaff().getFirstNm() + " " + schedule.getStaff().getLastNm());

		} else {
			item.setInstructor("Not Assigned");
		}

		return item;
	}
	// =========================================================
	// GET AUTHENTICATED STAFF
	// =========================================================

	private Staff getAuthenticatedStaff() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {

			throw new RuntimeException("Authenticated staff is required");
		}

		// Your JWT authentication returns email
		String email = authentication.getName();

		return staffRepository.findByEmailId(email)
				.orElseThrow(() -> new RuntimeException("Staff not found with email: " + email));
	}

	@Override
	public List<EnrollmentBatchResponse> getEnrolledBatchesByStudentId(String studentId) {

		List<EnrollmentBatch> enrollmentBatches = enrollmentBatchRepository.findByEnrollmentStudentStudentId(studentId);

		if (enrollmentBatches.isEmpty()) {
			return List.of();
		}

		return enrollmentBatches.stream().map(mapper::toResponse).toList();
	}
}