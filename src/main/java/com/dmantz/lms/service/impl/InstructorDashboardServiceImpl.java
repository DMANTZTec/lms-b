package com.dmantz.lms.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.InstructorTaskRequest;
import com.dmantz.lms.dto.response.InstructorBatchResponse;
import com.dmantz.lms.dto.response.InstructorBatchSummaryResponse;
import com.dmantz.lms.dto.response.InstructorClassStatsResponse;
import com.dmantz.lms.dto.response.InstructorStudentStatsResponse;
import com.dmantz.lms.dto.response.InstructorTaskResponse;
import com.dmantz.lms.entity.AssignedByType;
import com.dmantz.lms.entity.Chapter;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.ClassStatus;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentBatch;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskStatus;
import com.dmantz.lms.entity.Topic;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.UnauthorizedAccessException;
import com.dmantz.lms.mapper.StudentTaskMapper;
import com.dmantz.lms.repository.ChapterRepository;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.ClassScheduleRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StudentTaskRepository;
import com.dmantz.lms.repository.TopicRepository;
import com.dmantz.lms.service.InstructorDashboardService;

@Service
public class InstructorDashboardServiceImpl implements InstructorDashboardService {

	private static final Logger logger = LogManager.getLogger(InstructorDashboardServiceImpl.class);

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

	public InstructorDashboardServiceImpl(StaffRepository staffRepository, ClassBatchRepository classBatchRepository,
			CourseRepository courseRepository, ChapterRepository chapterRepository, TopicRepository topicRepository,
			EnrollmentBatchRepository enrollmentBatchRepository, EnrollmentRepository enrollmentRepository,
			StudentTaskRepository studentTaskRepository, StudentTaskMapper studentTaskMapper,
			ClassScheduleRepository classScheduleRepository) {
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
	}

	@Override
	@Transactional
	public InstructorTaskResponse createTask(InstructorTaskRequest request) {

		Staff instructor = staffRepository.findByStaffId(request.getAssignedBy())
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + request.getAssignedBy()));

		logger.info("Instructor {} creating task for batchId: {} courseId: {}", instructor.getStaffId(),
				request.getBatchId(), request.getCourseId());

		ClassBatch batch = classBatchRepository.findById(request.getBatchId())
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + request.getBatchId()));

		boolean assignedToBatch = classBatchRepository.existsByIdAndInstructorsStaffId(batch.getId(),
				instructor.getStaffId());
		if (!assignedToBatch) {
			throw new UnauthorizedAccessException("Instructor is not assigned to batch: " + request.getBatchId());
		}

		Course course = courseRepository.findByCourseId(request.getCourseId())
				.orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

		if (batch.getCourse() == null || !batch.getCourse().getId().equals(course.getId())) {
			throw new IllegalArgumentException("Selected batch does not belong to the selected course");
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

		List<EnrollmentBatch> enrollmentBatches = enrollmentBatchRepository
				.findWithStudentsByClassBatchId(batch.getId());

		Map<String, Student> uniqueStudents = new LinkedHashMap<>();
		for (EnrollmentBatch enrollmentBatch : enrollmentBatches) {
			Enrollment enrollment = enrollmentBatch.getEnrollment();
			if (enrollment == null || enrollment.getStudent() == null) {
				continue;
			}
			if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
				continue;
			}
			uniqueStudents.putIfAbsent(enrollment.getStudent().getStudentId(), enrollment.getStudent());
		}

		List<Student> students = new ArrayList<>(uniqueStudents.values());
		if (students.isEmpty()) {
			throw new ResourceNotFoundException("No students are enrolled in batch: " + batch.getId());
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
			task.setBatchId(batch.getId());
			task.setClassBatch(batch);
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

		logger.info("Instructor {} assigned task '{}' to {} students in batch {}", instructor.getStaffId(),
				request.getTitle(), savedTasks.size(), batch.getId());

		return new InstructorTaskResponse(request.getTitle(), request.getDescription(), course.getCourseId(),
				batch.getId(), savedTasks.size(), savedTasks.stream().map(studentTaskMapper::toResponse).toList());
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
}