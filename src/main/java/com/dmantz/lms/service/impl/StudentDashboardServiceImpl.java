package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.ClassBatchMapper;
import com.dmantz.lms.mapper.ClassScheduleMapper;
import com.dmantz.lms.mapper.StudentCourseMapper;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.StudentDashboardService;

import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentDashboardServiceImpl implements StudentDashboardService {

	private static final Logger logger = LogManager.getLogger(StudentDashboardServiceImpl.class);

	private final ClassScheduleRepository classScheduleRepository;
	private final ClassScheduleMapper classScheduleMapper;
	private final ClassBatchRepository classBatchRepository;
	private final StaffRepository staffRepository;
	private final StudentCourseRepository studentCourseRepository;
	private final StudentCourseMapper studentCourseMapper;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final StudentTopicReferenceProgressRepository progressRepository;
	private final ClassBatchMapper classBatchMapper;

	public StudentDashboardServiceImpl(ClassScheduleRepository classScheduleRepository,
			ClassScheduleMapper classScheduleMapper, ClassBatchRepository classBatchRepository,
			StaffRepository staffRepository, StudentCourseRepository studentCourseRepository,
			StudentCourseMapper studentCourseMapper, StudentRepository studentRepository,
			CourseRepository courseRepository, StudentTopicReferenceProgressRepository progressRepository,
			ClassBatchMapper classBatchMapper) {

		this.classScheduleRepository = classScheduleRepository;
		this.classScheduleMapper = classScheduleMapper;
		this.classBatchRepository = classBatchRepository;
		this.staffRepository = staffRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.studentCourseMapper = studentCourseMapper;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
		this.progressRepository = progressRepository;
		this.classBatchMapper = classBatchMapper;
	}

	// ================= WEEKLY SCHEDULE =================

	@Override
	public WeeklyScheduleResponse getWeeklySchedule(String studentId) {

		logger.info("Fetching weekly schedule for studentId: {}", studentId);

		LocalDate today = LocalDate.now();
		LocalDate weekStart = today.with(DayOfWeek.MONDAY);
		LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);

		List<ClassSchedule> schedules = classScheduleRepository.findWeeklySchedule(studentId, weekStart, weekEnd,
				ClassStatus.SCHEDULED);

		List<ClassScheduleResponse> classDtos = classScheduleMapper.toDtoList(schedules);

		WeeklyScheduleResponse response = new WeeklyScheduleResponse();
		response.setStudentId(studentId);
		response.setWeekStart(weekStart);
		response.setWeekEnd(weekEnd);
		response.setTotalClasses((long) classDtos.size());
		response.setClasses(classDtos);

		logger.info("Weekly schedule fetched successfully for studentId: {}", studentId);

		return response;
	}

	// ================= MY COURSES =================

	@Override
	public StudentMyCoursesResponse getMyCourses(String studentId, CourseStatus status) {

		logger.info("Fetching courses for studentId: {}", studentId);

		List<StudentCourse> allCourses = studentCourseRepository.findByStudent_StudentId(studentId);

		if (allCourses.isEmpty()) {
			logger.warn("No courses found for studentId: {}", studentId);
			throw new ResourceNotFoundException("No courses found for student: " + studentId);
		}

		List<MyCourseResponse> courseResponses = new ArrayList<>();

		long planned = 0;
		long ongoing = 0;
		long completed = 0;

		for (StudentCourse sc : allCourses) {

			CourseProgressSummaryResponse progress = getCourseProgressSummary(sc.getCourse().getCourseId(),
					sc.getStudent().getStudentId());

			double percentage = progress.getCoursePercentage();

			CourseStatus derivedStatus;

			if (percentage == 0) {
				derivedStatus = CourseStatus.PLANNED;
				planned++;
			} else if (percentage == 100) {
				derivedStatus = CourseStatus.COMPLETED;
				completed++;
			} else {
				derivedStatus = CourseStatus.ACTIVE;
				ongoing++;
			}

			MyCourseResponse dto = new MyCourseResponse();
			dto.setCourseId(sc.getCourse().getCourseId());
			dto.setCourseName(sc.getCourse().getCourseTitle());
			dto.setStatus(derivedStatus.name());
			dto.setProgress(percentage);
			dto.setStartDate(sc.getStart_dt() != null ? sc.getStart_dt().toLocalDate() : null);

			dto.setEndDate(sc.getCompletedDt() != null ? sc.getCompletedDt().toLocalDate() : null);

			courseResponses.add(dto);
		}

		if (status != null) {
			courseResponses = courseResponses.stream().filter(c -> c.getStatus().equals(status.name())).toList();
		}

		StudentMyCoursesResponse response = new StudentMyCoursesResponse();

		response.setTotalCourses(allCourses.size());
		response.setPlanned(planned);
		response.setOngoing(ongoing);
		response.setCompleted(completed);
		response.setCourses(courseResponses);

		logger.info("Courses fetched successfully for studentId: {}", studentId);

		return response;
	}

	// ================= TOPIC PROGRESS =================

	@Override
	public List<TopicProgressResponse> getTopicProgress(String courseId, String studentId) {

		logger.info("Fetching topic progress for courseId: {} and studentId: {}", courseId, studentId);

		Course course = courseRepository.findByCourseIdAndIsDeletedFalse(courseId).orElseThrow(() -> {
			logger.error("Course not found with courseId: {}", courseId);
			return new ResourceNotFoundException("Course not found: " + courseId);
		});

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found with studentId: {}", studentId);
			return new ResourceNotFoundException("Student not found: " + studentId);
		});

		Long internalStudentId = student.getId();
		Long internalCourseId = course.getId();

		boolean enrolled = studentCourseRepository.findByStudent_IdAndCourse_Id(internalStudentId, internalCourseId)
				.isPresent();

		if (!enrolled) {
			logger.error("Student {} not enrolled in course {}", studentId, courseId);

			throw new ResourceNotFoundException("Student " + studentId + " not enrolled in course " + courseId);
		}

		List<TopicProgressResponse> response = new ArrayList<>();

		for (Chapter chapter : course.getChapters()) {

			for (Topic topic : chapter.getTopics()) {

				List<TopicReference> references = topic.getReferences();

				int totalReferences = references == null ? 0 : references.size();

				List<StudentTopicReferenceProgress> studentProgress = progressRepository
						.findByStudent_IdAndTopicReference_Topic_Id(internalStudentId, topic.getId());

				Set<Long> completedReferenceIds = studentProgress.stream()
						.filter(p -> Boolean.TRUE.equals(p.getCompleted())).map(p -> p.getTopicReference().getId())
						.collect(Collectors.toSet());

				long completedCount = references == null ? 0
						: references.stream().filter(ref -> completedReferenceIds.contains(ref.getId())).count();

				double percentage = totalReferences == 0 ? 0.0 : (completedCount * 100.0) / totalReferences;

				TopicProgressResponse dto = new TopicProgressResponse();

				dto.setTopicId(topic.getId());
				dto.setTopicName(topic.getTopicNm());
				dto.setProgressPercentage(percentage);
				dto.setCompletedTopicReference((int) completedCount);
				dto.setTotalTopicReference(totalReferences);
				dto.setCompleted(totalReferences > 0 && completedCount == totalReferences);

				response.add(dto);
			}
		}

		logger.info("Topic progress fetched successfully for courseId: {} and studentId: {}", courseId, studentId);

		return response;
	}

	// ================= CHAPTER PROGRESS =================

	@Override
	public List<ChapterProgressResponse> getChapterProgress(String courseId, String studentId) {

		logger.info("Fetching chapter progress for courseId: {} and studentId: {}", courseId, studentId);

		Course course = courseRepository.findByCourseIdAndIsDeletedFalse(courseId).orElseThrow(() -> {
			logger.error("Course not found with courseId: {}", courseId);
			return new ResourceNotFoundException("Course not found: " + courseId);
		});

		studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found with studentId: {}", studentId);
			return new ResourceNotFoundException("Student not found: " + studentId);
		});

		List<TopicProgressResponse> topicProgressList = getTopicProgress(courseId, studentId);

		List<ChapterProgressResponse> response = new ArrayList<>();

		for (Chapter chapter : course.getChapters()) {

			List<Topic> topics = chapter.getTopics();

			int totalTopics = topics == null ? 0 : topics.size();

			int completedTopics = 0;

			if (topics != null) {

				for (Topic topic : topics) {

					TopicProgressResponse topicProgress = topicProgressList.stream()
							.filter(t -> t.getTopicId().equals(topic.getId())).findFirst().orElse(null);

					if (topicProgress != null && Boolean.TRUE.equals(topicProgress.getCompleted())) {

						completedTopics++;
					}
				}
			}

			double percentage = totalTopics == 0 ? 0.0 : (completedTopics * 100.0) / totalTopics;

			ChapterProgressResponse dto = new ChapterProgressResponse();

			dto.setChapterId(chapter.getId());
			dto.setChapterName(chapter.getChapterNm());
			dto.setCompletedTopics(completedTopics);
			dto.setTotalTopics(totalTopics);
			dto.setChapterPercentage(percentage);
			dto.setCompleted(totalTopics > 0 && completedTopics == totalTopics);

			response.add(dto);
		}

		logger.info("Chapter progress fetched successfully for courseId: {} and studentId: {}", courseId, studentId);

		return response;
	}

	// ================= COURSE SUMMARY =================

	@Override
	public CourseProgressSummaryResponse getCourseProgressSummary(String courseId, String studentId) {

		logger.info("Fetching course progress summary for courseId: {} and studentId: {}", courseId, studentId);

		Course course = courseRepository.findByCourseIdAndIsDeletedFalse(courseId).orElseThrow(() -> {
			logger.error("Course not found with courseId: {}", courseId);
			return new ResourceNotFoundException("Course not found: " + courseId);
		});

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found with studentId: {}", studentId);
			return new ResourceNotFoundException("Student not found: " + studentId);
		});

		Long internalStudentId = student.getId();
		Long internalCourseId = course.getId();

		int totalChapters = 0;
		int completedChapters = 0;
		int totalTopics = 0;
		int completedTopics = 0;
		int totalReferences = 0;
		int completedReferences = 0;

		for (Chapter chapter : course.getChapters()) {

			totalChapters++;

			boolean isChapterCompleted = true;

			for (Topic topic : chapter.getTopics()) {

				totalTopics++;

				List<TopicReference> references = topic.getReferences();

				int topicTotalReferences = references == null ? 0 : references.size();

				totalReferences += topicTotalReferences;

				List<StudentTopicReferenceProgress> progressList = progressRepository
						.findByStudent_IdAndTopicReference_Topic_Id(internalStudentId, topic.getId());

				long topicCompletedReferences = progressList.stream().filter(p -> Boolean.TRUE.equals(p.getCompleted()))
						.count();

				completedReferences += topicCompletedReferences;

				if (topicTotalReferences > 0 && topicCompletedReferences == topicTotalReferences) {

					completedTopics++;

				} else {

					isChapterCompleted = false;
				}
			}

			if (isChapterCompleted && !chapter.getTopics().isEmpty()) {

				completedChapters++;
			}
		}

		double percentage = totalReferences == 0 ? 0.0 : (completedReferences * 100.0) / totalReferences;

		StudentCourse sc = studentCourseRepository.findByStudent_IdAndCourse_Id(internalStudentId, internalCourseId)
				.orElseThrow(() -> {
					logger.error("Enrollment not found for studentId: {} and courseId: {}", studentId, courseId);

					return new ResourceNotFoundException("Enrollment not found");
				});

		if (percentage == 0) {
			sc.setStatus(CourseStatus.PLANNED);
		} else if (percentage == 100) {
			sc.setStatus(CourseStatus.COMPLETED);
		} else {
			sc.setStatus(CourseStatus.ACTIVE);
		}

		if (percentage > 0 && sc.getStart_dt() == null) {
			sc.setStart_dt(LocalDateTime.now());
		}

		if (percentage == 100 && sc.getCompletedDt() == null) {
			sc.setCompletedDt(LocalDateTime.now());
		}

		studentCourseRepository.save(sc);

		CourseProgressSummaryResponse response = new CourseProgressSummaryResponse();

		response.setCourseId(course.getCourseId());
		response.setCourseName(course.getCourseTitle());
		response.setTotalChapters(totalChapters);
		response.setCompletedChapters(completedChapters);
		response.setTotalTopics(totalTopics);
		response.setCompletedTopics(completedTopics);
		response.setTotalReferences(totalReferences);
		response.setCompletedReferences(completedReferences);
		response.setCoursePercentage(percentage);
		response.setCompleted(totalReferences > 0 && completedReferences == totalReferences);

		logger.info("Course progress summary fetched successfully for courseId: {} and studentId: {}", courseId,
				studentId);

		return response;
	}

	// ================= CLASS INFO =================

	@Override
	public List<StudentClassResponse> getClassInfo(String studentId) {

		logger.info("Fetching class info for studentId: {}", studentId);

		List<ClassBatch> batches = classBatchRepository.findByStudentId(Long.valueOf(studentId));

		if (batches.isEmpty()) {
			logger.warn("No class batches found for studentId: {}", studentId);

			throw new ResourceNotFoundException("No class information found for student: " + studentId);
		}

		logger.info("Class info fetched successfully for studentId: {}", studentId);

		return batches.stream().map(classBatchMapper::toDto).toList();
	}

	// ================= DASHBOARD SUMMARY =================

	@Override
	public StudentDashboardSummaryResponse getDashboardSummary(String studentId) {

		logger.info("Fetching dashboard summary for studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found with studentId: {}", studentId);

			return new ResourceNotFoundException("Student not found: " + studentId);
		});

		StudentMyCoursesResponse courses = getMyCourses(studentId, null);

		WeeklyScheduleResponse weeklySchedule = getWeeklySchedule(studentId);

		int grandTotal = 0;
		int grandCompleted = 0;

		for (MyCourseResponse course : courses.getCourses()) {

			CourseProgressSummaryResponse summary = getCourseProgressSummary(course.getCourseId(), studentId);

			grandTotal += summary.getTotalReferences();
			grandCompleted += summary.getCompletedReferences();
		}

		OverallProgressResponse overallProgress = new OverallProgressResponse();

		overallProgress.setTotalReferences(grandTotal);
		overallProgress.setCompletedReferences(grandCompleted);

		overallProgress.setOverallPercentage(
				grandTotal == 0 ? 0.0 : Math.round((grandCompleted * 100.0 / grandTotal) * 100.0) / 100.0);

		overallProgress.setCompleted(grandTotal > 0 && grandCompleted == grandTotal);

		StudentDashboardSummaryResponse response = new StudentDashboardSummaryResponse();

		response.setStudentId(studentId);
		response.setStudentName(student.getFirstNm() + " " + student.getLastNm());
		response.setProfileImg(student.getProfileImg());
		response.setCourses(courses);
		response.setWeeklySchedule(weeklySchedule);
		response.setOverallProgress(overallProgress);

		logger.info("Dashboard summary fetched successfully for studentId: {}", studentId);

		return response;
	}
}