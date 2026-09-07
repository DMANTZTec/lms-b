package com.dmantz.lms.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.UnauthorizedAccessException;
import com.dmantz.lms.mapper.StudentTaskMapper;
import com.dmantz.lms.mapper.StudentTaskSubmissionMapper;
import com.dmantz.lms.repository.ChapterRepository;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.ClassScheduleRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.StaffCourseRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StudentTaskRepository;
import com.dmantz.lms.repository.StudentTaskSubmissionRepository;
import com.dmantz.lms.repository.TopicRepository;
import com.dmantz.lms.service.InstructorDashboardService;

@Service
public class InstructorDashboardServiceImpl implements InstructorDashboardService {

	private static final Logger logger = LogManager.getLogger(InstructorDashboardServiceImpl.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final StaffRepository staffRepository;
	private final ClassBatchRepository classBatchRepository;
	private final CourseRepository courseRepository;
	private final ChapterRepository chapterRepository;
	private final TopicRepository topicRepository;
	private final EnrollmentBatchRepository enrollmentBatchRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final StudentTaskRepository studentTaskRepository;
	private final StudentTaskMapper studentTaskMapper;
	private final ClassScheduleRepository classScheduleRepository;
	private final StaffCourseRepository staffCourseRepository;
	private final StudentTaskSubmissionRepository studentTaskSubmissionRepository;
	private final StudentTaskSubmissionMapper studentTaskSubmissionMapper;

	

	public InstructorDashboardServiceImpl(StaffRepository staffRepository, ClassBatchRepository classBatchRepository,
			CourseRepository courseRepository, ChapterRepository chapterRepository, TopicRepository topicRepository,
			EnrollmentBatchRepository enrollmentBatchRepository, EnrollmentRepository enrollmentRepository,
			StudentTaskRepository studentTaskRepository, StudentTaskMapper studentTaskMapper,
			ClassScheduleRepository classScheduleRepository, StaffCourseRepository staffCourseRepository,
			StudentTaskSubmissionRepository studentTaskSubmissionRepository,
			StudentTaskSubmissionMapper studentTaskSubmissionMapper) {
		super();
		this.staffRepository = staffRepository;
		this.classBatchRepository = classBatchRepository;
		this.courseRepository = courseRepository;
		this.chapterRepository = chapterRepository;
		this.topicRepository = topicRepository;
		this.enrollmentBatchRepository = enrollmentBatchRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.studentTaskRepository = studentTaskRepository;
		this.studentTaskMapper = studentTaskMapper;
		this.classScheduleRepository = classScheduleRepository;
		this.staffCourseRepository = staffCourseRepository;
		this.studentTaskSubmissionRepository = studentTaskSubmissionRepository;
		this.studentTaskSubmissionMapper = studentTaskSubmissionMapper;
	}

	@Override
	@Transactional
	public InstructorTaskResponse createTask(InstructorTaskRequest request) {

		Staff instructor = staffRepository.findByStaffId(request.getAssignedBy())
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + request.getAssignedBy()));

		logger.info("Instructor {} creating task for courseId: {}", instructor.getStaffId(), request.getCourseId());

		Course course = courseRepository.findByCourseId(request.getCourseId())
				.orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

		boolean assignedToCourse = staffCourseRepository
				.existsByStaff_StaffIdAndCourse_CourseId(instructor.getStaffId(), course.getCourseId());
		if (!assignedToCourse) {
			throw new UnauthorizedAccessException(
					"Instructor is not assigned to course: " + request.getCourseId());
		}

		Chapter chapter = chapterRepository.findById(request.getChapterId())
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found: " + request.getChapterId()));
		if (!chapter.getCourse().getCourseId().equals(course.getCourseId())) {
			throw new IllegalArgumentException("Selected chapter does not belong to the selected course");
		}

		Topic topic = topicRepository.findById(request.getTopicId())
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + request.getTopicId()));
		if (topic.getChapter() == null || !topic.getChapter().getId().equals(chapter.getId())) {
			throw new IllegalArgumentException("Selected topic does not belong to the selected chapter");
		}

		List<Enrollment> enrollments = enrollmentRepository
				.findByCourse_CourseIdAndStatusNot(course.getCourseId(), EnrollmentStatus.CANCELLED);

		Map<String, Student> uniqueStudents = new LinkedHashMap<>();
		for (Enrollment enrollment : enrollments) {
			if (enrollment.getStudent() == null) {
				continue;
			}
			uniqueStudents.putIfAbsent(enrollment.getStudent().getStudentId(), enrollment.getStudent());
		}

		List<Student> students = new ArrayList<>(uniqueStudents.values());
		if (students.isEmpty()) {
			throw new ResourceNotFoundException("No students are enrolled in course: " + course.getCourseId());
		}

		LocalDateTime now = LocalDateTime.now();
		List<StudentTask> savedTasks = new ArrayList<>();

		for (Student student : students) {
			StudentTask task = new StudentTask();
			task.setTitle(request.getTitle());
			task.setDescription(request.getDescription());
			task.setCourseId(course.getCourseId());
			task.setCourse(course);
			task.setChapter(chapter);
			task.setTopic(topic);
			task.setStudent(student);
			task.setAssignedBy(instructor.getStaffId());
			task.setAssignedByType(AssignedByType.INSTRUCTOR);
			task.setStatus(StudentTaskStatus.ACTIVE);
			task.setStartDt(now);
			task.setNeedHelp(false);
			task.setCreatedBy(instructor.getId());
			task.setCreatedDt(now);
			task.setUpdatedBy(instructor.getId());
			task.setUpdatedDt(now);
			savedTasks.add(studentTaskRepository.save(task));
		}

		logger.info("Instructor {} assigned task '{}' to {} students in course {}", instructor.getStaffId(),
				request.getTitle(), savedTasks.size(), course.getCourseId());

		return new InstructorTaskResponse(request.getTitle(), request.getDescription(), course.getCourseId(),
				savedTasks.size(), savedTasks.stream().map(studentTaskMapper::toResponse).toList());
	}
	@Override
	public InstructorBatchSummaryResponse getBatchSummary(String instructorId) {

		Staff instructor = staffRepository.findByStaffId(instructorId)
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + instructorId));

		logger.info("Fetching active/completed batch summary for instructor: {}", instructor.getStaffId());

		List<ClassBatch> batches = classBatchRepository.findByInstructors_StaffId(instructor.getStaffId());

		List<InstructorBatchResponse> activeBatches = new ArrayList<>();
		List<InstructorBatchResponse> completedBatches = new ArrayList<>();

		for (ClassBatch batch : batches) {
			if (batch.getStatus() != ClassStatus.SCHEDULED && batch.getStatus() != ClassStatus.COMPLETED) {
				// CANCELLED batches are neither active nor completed
				continue;
			}

			boolean isCompleted = batch.getStatus() == ClassStatus.COMPLETED;

			InstructorBatchResponse batchResponse = new InstructorBatchResponse(batch.getId(), batch.getClassName(),
					batch.getCourse() != null ? batch.getCourse().getCourseId() : null,
					batch.getCourse() != null ? batch.getCourse().getCourseTitle() : null, batch.getStartDate(),
					batch.getEndDate(), isCompleted ? "COMPLETED" : "ACTIVE");

			if (isCompleted) {
				completedBatches.add(batchResponse);
			} else {
				activeBatches.add(batchResponse);
			}
		}

		logger.info("Instructor {} has {} active and {} completed batches", instructor.getStaffId(),
				activeBatches.size(), completedBatches.size());

		return new InstructorBatchSummaryResponse(activeBatches.size(), completedBatches.size(), activeBatches,
				completedBatches);
	}

	@Override
	public InstructorClassStatsResponse getClassStats(String instructorId) {

		Staff instructor = staffRepository.findByStaffId(instructorId)
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + instructorId));

		logger.info("Fetching classes-taken/scheduled/hours-spent stats for instructor: {}", instructor.getStaffId());

		List<ClassSchedule> schedules = classScheduleRepository.findAllForInstructor(instructor.getStaffId());

		int classesTaken = 0;
		int scheduled = 0;
		long totalMinutes = 0;

		for (ClassSchedule schedule : schedules) {
			if (schedule.getStatus() == ClassStatus.COMPLETED) {
				classesTaken++;
				if (schedule.getStartTime() != null && schedule.getEndTime() != null) {
					totalMinutes += Duration.between(schedule.getStartTime(), schedule.getEndTime()).toMinutes();
				}
			} else if (schedule.getStatus() == ClassStatus.SCHEDULED) {
				scheduled++;
			}
		}

		double hoursSpent = Math.round((totalMinutes / 60.0) * 10) / 10.0;

		logger.info("Instructor {} has classesTaken: {}, scheduled: {}, hoursSpent: {}", instructor.getStaffId(),
				classesTaken, scheduled, hoursSpent);

		return new InstructorClassStatsResponse(classesTaken, scheduled, hoursSpent);
	}

	@Override
	public InstructorStudentStatsResponse getStudentStats(String instructorId) {

		Staff instructor = staffRepository.findByStaffId(instructorId)
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + instructorId));

		logger.info("Fetching active/total student stats for instructor: {}", instructor.getStaffId());

		List<ClassBatch> batches = classBatchRepository.findByInstructors_StaffId(instructor.getStaffId());

		if (batches.isEmpty()) {
			return new InstructorStudentStatsResponse(0, 0);
		}

		List<Long> batchIds = batches.stream().map(ClassBatch::getId).distinct().toList();
		List<Long> courseIds = batches.stream().map(ClassBatch::getCourse).filter(course -> course != null)
				.map(Course::getId).distinct().toList();

		// Active = students placed into one of this instructor's batches.
		Set<String> activeStudentIds = new LinkedHashSet<>(
				enrollmentBatchRepository.findDistinctStudentIdsByClassBatchIds(batchIds));

		// Total = students enrolled (directly or via a program) in one of this instructor's courses.
		Set<String> totalStudentIds = new LinkedHashSet<>();
		totalStudentIds.addAll(enrollmentRepository.findDistinctStudentIdsByCourseIds(courseIds));
		totalStudentIds.addAll(enrollmentRepository.findDistinctStudentIdsByProgramCourseIds(courseIds));
		totalStudentIds.addAll(activeStudentIds);

		logger.info("Instructor {} has {} active and {} total students", instructor.getStaffId(),
				activeStudentIds.size(), totalStudentIds.size());

		return new InstructorStudentStatsResponse(activeStudentIds.size(), totalStudentIds.size());
	}
	
	
	@Override
	public List<StudentTaskSubmissionResponse> getTaskSubmissions(String staffId, String courseId) {

		Staff instructor = staffRepository.findByStaffId(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + staffId));

		List<StudentTaskSubmission> submissions;

		if (courseId != null && !courseId.isBlank()) {

			boolean assignedToCourse = staffCourseRepository
					.existsByStaff_StaffIdAndCourse_CourseId(instructor.getStaffId(), courseId);
			if (!assignedToCourse) {
				throw new UnauthorizedAccessException(
						"Instructor " + instructor.getStaffId() + " is not assigned to course: " + courseId);
			}

			submissions = studentTaskSubmissionRepository.findByStudentTask_CourseId(courseId);

			logger.info("Instructor {} retrieved {} submission(s) for course {}", instructor.getStaffId(),
					submissions.size(), courseId);

		} else {

			List<String> courseIds = staffCourseRepository.findByStaff_StaffId(instructor.getStaffId()).stream()
					.map(StaffCourse::getCourse)
					.filter(c -> c != null)
					.map(Course::getCourseId)
					.distinct()
					.toList();

			if (courseIds.isEmpty()) {
				throw new ResourceNotFoundException(
						"Instructor " + instructor.getStaffId() + " is not assigned to any course");
			}

			submissions = studentTaskSubmissionRepository.findByStudentTask_CourseIdIn(courseIds);

			logger.info("Instructor {} retrieved {} submission(s) across {} course(s)", instructor.getStaffId(),
					submissions.size(), courseIds.size());
		}

		return submissions.stream().map(studentTaskSubmissionMapper::toResponse).toList();
	}

	@Override
	public List<InstructorCourseResponse> getMyCourses(String instructorId) {

		Staff instructor = staffRepository.findByStaffId(instructorId)
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + instructorId));

		logger.info("Fetching my-courses payload for instructor: {}", instructor.getStaffId());

		// All courses the instructor is assigned to (StaffCourse).
		List<Course> assignedCourses = staffCourseRepository.findByStaff_StaffId(instructor.getStaffId()).stream()
				.map(StaffCourse::getCourse)
				.filter(c -> c != null && !c.isDeleted())
				.distinct()
				.toList();

		if (assignedCourses.isEmpty()) {
			logger.info("Instructor {} is not assigned to any course", instructor.getStaffId());
			return List.of();
		}

		List<String> courseIds = assignedCourses.stream().map(Course::getCourseId).distinct().toList();
		Set<Long> assignedCourseIds = new LinkedHashSet<>(
				assignedCourses.stream().map(Course::getId).toList());

		// All batches this instructor teaches.
		List<ClassBatch> instructorBatches = classBatchRepository.findByInstructors_StaffId(instructor.getStaffId());

		// Group schedule rows per course for upcoming-class / date computation.
		List<Long> instructorBatchIds = instructorBatches.stream().map(ClassBatch::getId).distinct().toList();
		List<ClassSchedule> instructorSchedules = instructorBatchIds.isEmpty() ? List.of()
				: classScheduleRepository.findByClassBatchIdInAndClassDateBetweenOrderByClassDateAscStartTimeAsc(
				instructorBatchIds, LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));

		LocalDate today = LocalDate.now();
		List<InstructorCourseResponse> result = new ArrayList<>();

		for (Course course : assignedCourses) {

			if (course == null) {
				continue;
			}

			InstructorCourseResponse resp = new InstructorCourseResponse();
			resp.setCourseId(course.getCourseId());
			resp.setTitle(course.getCourseTitle());
			resp.setCourseImage(course.getCourseImage());
			resp.setDescription(course.getDescription());
			resp.setLanguage(course.getLanguage());
			resp.setSubject(course.getSubject() != null ? course.getSubject().getSubjectNm() : null);
			resp.setLevel(displayLevel(course.getLevel()));
			resp.setSkills(parseSkills(course.getSkills()));

			// Chapters
			List<Chapter> chapters = chapterRepository.findByCourse_CourseId(course.getCourseId());
			int totalChapters = chapters.size();
			int chaptersCompleted = (int) chapters.stream().filter(ch -> ch.getTopics() != null && !ch.getTopics().isEmpty())
					.count();
			resp.setTotalChapters(totalChapters);
			resp.setChaptersCompleted(chaptersCompleted);
			resp.setProgress(totalChapters == 0 ? 0.0
					: Math.round((chaptersCompleted * 100.0 / totalChapters) * 10.0) / 10.0);

			// This course's batches (only those this instructor teaches).
			List<ClassBatch> courseBatches = instructorBatches.stream()
					.filter(b -> b.getCourse() != null && b.getCourse().getId().equals(course.getId()))
					.toList();

			boolean hasActiveBatch = false;
			boolean hasCompletedBatch = false;
			boolean hasBatch = false;
			int totalStudents = 0;
			Set<String> activeStudentIds = new LinkedHashSet<>();

			for (ClassBatch batch : courseBatches) {
				hasBatch = true;
				if (batch.getStatus() == ClassStatus.SCHEDULED) {
					hasActiveBatch = true;
				} else if (batch.getStatus() == ClassStatus.COMPLETED) {
					hasCompletedBatch = true;
				}

				int batchStudents = (int) enrollmentBatchRepository.countByClassBatchId(batch.getId());
				totalStudents += batchStudents;

				// Batch detail (name, students, schedule).
				InstructorCourseResponse.BatchDetail detail = new InstructorCourseResponse.BatchDetail();
				detail.setName(batch.getClassName());
				detail.setStudents(batchStudents);
				detail.setSchedule(buildBatchSchedule(batch, instructorSchedules, today));
				resp.getBatches().add(detail);

				// Active students (placed into one of this instructor's active batches).
				if (batch.getStatus() == ClassStatus.SCHEDULED) {
					activeStudentIds
							.addAll(enrollmentBatchRepository.findDistinctStudentIdsByClassBatchIds(List.of(batch.getId())));
				}
			}

			resp.setTotalStudents(totalStudents);
			resp.setActiveStudents(activeStudentIds.size());

			// Status: derive from batches.
			String status;
			if (!hasBatch) {
				status = "PLANNED";
			} else if (hasActiveBatch) {
				status = "ACTIVE";
			} else if (hasCompletedBatch) {
				status = "COMPLETED";
			} else {
				status = "PLANNED";
			}
			resp.setStatus(status);

			// Upcoming classes + next/last class date (from schedules belonging to batches of this course).
			Set<Long> courseBatchIds = courseBatches.stream().map(ClassBatch::getId).collect(java.util.stream.Collectors.toSet());
			int upcomingClasses = 0;
			for (ClassSchedule schedule : instructorSchedules) {
				if (!courseBatchIds.contains(schedule.getClassBatch() == null ? null : schedule.getClassBatch().getId())) {
					continue;
				}
				if (schedule.getClassDate() != null && schedule.getStatus() == ClassStatus.SCHEDULED) {
					if (schedule.getClassDate().compareTo(today) >= 0) {
						upcomingClasses++;
						if (resp.getNextClassDate() == null
								|| schedule.getClassDate().isBefore(resp.getNextClassDate())) {
							resp.setNextClassDate(schedule.getClassDate());
						}
					}
					if (resp.getLastClassDate() == null
							|| schedule.getClassDate().isAfter(resp.getLastClassDate())) {
						resp.setLastClassDate(schedule.getClassDate());
					}
				}
			}
			resp.setUpcomingClasses(upcomingClasses);

			// Avoid returning batches linked to courses not actually assigned to this instructor.
			if (!assignedCourseIds.contains(course.getId())) {
				continue;
			}

			result.add(resp);
		}

		logger.info("Instructor {} has {} my-course records", instructor.getStaffId(), result.size());
		return result;
	}

	private String displayLevel(CourseLevel level) {
		if (level == null) {
			return null;
		}
		String name = level.name();
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	private List<String> parseSkills(String skills) {
		if (skills == null || skills.isBlank()) {
			return List.of();
		}
		try {
			return OBJECT_MAPPER.readValue(skills, new TypeReference<List<String>>() {
			});
		} catch (Exception e) {
			logger.warn("Failed to parse course skills JSON: {}", skills);
			return List.of();
		}
	}

	private String buildBatchSchedule(ClassBatch batch, List<ClassSchedule> schedules, LocalDate today) {
		if (batch.getStatus() == ClassStatus.COMPLETED) {
			return "Completed";
		}
		if (batch.getStatus() == ClassStatus.SCHEDULED && batch.getEndDate() != null && batch.getEndDate().isBefore(today)) {
			return "Completed";
		}
		if (batch.getStartDate() != null && batch.getStartDate().isAfter(today)) {
			return "Starts " + batch.getStartDate();
		}
		// Combine schedule day/times for upcoming active classes.
		List<String> parts = new ArrayList<>();
		for (ClassSchedule schedule : schedules) {
			if (schedule.getClassBatch() == null || !schedule.getClassBatch().getId().equals(batch.getId())) {
				continue;
			}
			if (schedule.getStatus() != ClassStatus.SCHEDULED) {
				continue;
			}
			String day = schedule.getClassDate() != null
					? schedule.getClassDate().getDayOfWeek().toString().substring(0, 3)
					: "";
			String time = "";
			if (schedule.getStartTime() != null) {
				time = schedule.getStartTime().toString().substring(0, 5);
			}
			parts.add((day + " " + time).trim());
		}
		return parts.isEmpty() ? "Scheduled" : String.join(", ", parts);
	}
}