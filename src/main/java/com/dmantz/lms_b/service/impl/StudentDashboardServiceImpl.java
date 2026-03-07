package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.*;
import com.dmantz.lms_b.entity.*;

import com.dmantz.lms_b.exceptions.ResourceNotFoundException;

import com.dmantz.lms_b.mapper.ClassBatchMapper;
import com.dmantz.lms_b.mapper.ClassScheduleMapper;
import com.dmantz.lms_b.mapper.StudentCourseMapper;
import com.dmantz.lms_b.repository.ClassBatchRepository;
import com.dmantz.lms_b.repository.ClassScheduleRepository;
import com.dmantz.lms_b.repository.CourseRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.repository.StudentCourseRepository;
import com.dmantz.lms_b.repository.StudentRepository;
import com.dmantz.lms_b.repository.StudentTopicReferenceProgressRepository;
import com.dmantz.lms_b.repository.TopicReferenceRepository;
import com.dmantz.lms_b.service.StudentDashboardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentDashboardServiceImpl implements StudentDashboardService {

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
		super();
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

	@Override
	public WeeklyScheduleResponse getWeeklySchedule(String studentId) {

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

		return response;
	}

	@Override
	public StudentMyCoursesResponse getMyCourses(String studentId, CourseStatus status) {

		List<StudentCourse> allCourses = studentCourseRepository.findByStudent_StudentId(studentId);

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
				derivedStatus = CourseStatus.ONGOING;
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

		// Filter AFTER deriving status
		if (status != null) {
			courseResponses = courseResponses.stream().filter(c -> c.getStatus().equals(status.name())).toList();
		}

		StudentMyCoursesResponse response = new StudentMyCoursesResponse();
		response.setTotalCourses(allCourses.size());
		response.setPlanned(planned);
		response.setOngoing(ongoing);
		response.setCompleted(completed);
		response.setCourses(courseResponses);

		return response;
	}

	@Override
	public List<TopicProgressResponse> getTopicProgress(String courseId, String studentId) {

		// Bridge: String → entity → internal Long
		Course course = courseRepository.findByCourseId(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));

		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

		Long internalStudentId = student.getId(); // ← Long for all DB calls
		Long internalCourseId = course.getId(); // ← Long for all DB calls

		boolean enrolled = studentCourseRepository.findByStudent_IdAndCourse_Id(internalStudentId, internalCourseId)
				.isPresent();

		if (!enrolled) {
			throw new ResourceNotFoundException("Student " + studentId + " not enrolled in course " + courseId);
		}

		List<TopicProgressResponse> response = new ArrayList<>();

		for (Chapter chapter : course.getChapters()) {
			for (Topic topic : chapter.getTopics()) {

				List<TopicReference> references = topic.getReferences();
				int totalReferences = (references == null) ? 0 : references.size();

				List<StudentTopicReferenceProgress> studentProgress = progressRepository
						.findByStudent_IdAndTopicReference_Topic_Id(internalStudentId, topic.getId()); // ← Long

				Set<Long> completedReferenceIds = studentProgress.stream()
						.filter(p -> Boolean.TRUE.equals(p.getCompleted())).map(p -> p.getTopicReference().getId())
						.collect(Collectors.toSet());

				long completedCount = (references == null) ? 0
						: references.stream().filter(ref -> completedReferenceIds.contains(ref.getId())).count();

				double percentage = (totalReferences == 0) ? 0.0 : (completedCount * 100.0) / totalReferences;

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

		return response;
	}

	@Override
	public List<ChapterProgressResponse> getChapterProgress(String courseId, String studentId) {

		// Bridge: String → entity → internal Long
		Course course = courseRepository.findByCourseId(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));

		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

		Long internalStudentId = student.getId();
		Long internalCourseId = course.getId();

		// Reuse getTopicProgress — pass String ids (already bridged inside)
		List<TopicProgressResponse> topicProgressList = getTopicProgress(courseId, studentId);

		List<ChapterProgressResponse> response = new ArrayList<>();

		for (Chapter chapter : course.getChapters()) {

			List<Topic> topics = chapter.getTopics();
			int totalTopics = (topics == null) ? 0 : topics.size();
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

			double percentage = (totalTopics == 0) ? 0.0 : (completedTopics * 100.0) / totalTopics;

			ChapterProgressResponse dto = new ChapterProgressResponse();
			dto.setChapterId(chapter.getId());
			dto.setChapterName(chapter.getChapterNm());
			dto.setCompletedTopics(completedTopics);
			dto.setTotalTopics(totalTopics);
			dto.setChapterPercentage(percentage);
			dto.setCompleted(totalTopics > 0 && completedTopics == totalTopics);

			response.add(dto);
		}

		return response;
	}

	@Override
	public CourseProgressSummaryResponse getCourseProgressSummary(String courseId, String studentId) {

		// Bridge: String → entity → internal Long
		Course course = courseRepository.findByCourseId(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));

		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

		Long internalStudentId = student.getId(); // ← Long for all DB calls
		Long internalCourseId = course.getId(); // ← Long for all DB calls

		int totalChapters = 0, completedChapters = 0;
		int totalTopics = 0, completedTopics = 0;
		int totalReferences = 0, completedReferences = 0;

		for (Chapter chapter : course.getChapters()) {

			totalChapters++;
			boolean isChapterCompleted = true;

			for (Topic topic : chapter.getTopics()) {

				totalTopics++;

				List<TopicReference> references = topic.getReferences();
				int topicTotalReferences = (references == null) ? 0 : references.size();
				totalReferences += topicTotalReferences;

				List<StudentTopicReferenceProgress> progressList = progressRepository
						.findByStudent_IdAndTopicReference_Topic_Id(internalStudentId, topic.getId()); // ← Long

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

		double percentage = (totalReferences == 0) ? 0.0 : (completedReferences * 100.0) / totalReferences;

		StudentCourse sc = studentCourseRepository.findByStudent_IdAndCourse_Id(internalStudentId, internalCourseId) // ←
																														// Long
				.orElseThrow(() -> new RuntimeException("Enrollment not found"));
		if (percentage == 0) {
			sc.setStatus(CourseStatus.PLANNED);
		} else if (percentage == 100) {
			sc.setStatus(CourseStatus.COMPLETED);
		} else {
			sc.setStatus(CourseStatus.ONGOING);
		}

		if (percentage > 0 && sc.getStart_dt() == null) {
			sc.setStart_dt(LocalDateTime.now());
		}
		if (percentage == 100 && sc.getCompletedDt() == null) {
			sc.setCompletedDt(LocalDateTime.now());
		}
		studentCourseRepository.save(sc);
		CourseProgressSummaryResponse response = new CourseProgressSummaryResponse();
		response.setCourseId(course.getCourseId()); // ← String business ID
		response.setCourseName(course.getCourseTitle());
		response.setTotalChapters(totalChapters);
		response.setCompletedChapters(completedChapters);
		response.setTotalTopics(totalTopics);
		response.setCompletedTopics(completedTopics);
		response.setTotalReferences(totalReferences);
		response.setCompletedReferences(completedReferences);
		response.setCoursePercentage(percentage);
		response.setCompleted(totalReferences > 0 && completedReferences == totalReferences);

		return response;
	}

	@Override
	public List<StudentClassResponse> getClassInfo(String studentId) {

		List<ClassBatch> batches = classBatchRepository.findByStudentId(Long.valueOf(studentId));

		return batches.stream().map(classBatchMapper::toDto).toList();
	}

	@Override
	public StudentDashboardSummaryResponse getDashboardSummary(String studentId) {

		// Bridge once
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

		StudentMyCoursesResponse courses = getMyCourses(studentId, null);

		WeeklyScheduleResponse weeklySchedule = getWeeklySchedule(studentId);

		int grandTotal = 0;
		int grandCompleted = 0;

		for (MyCourseResponse course : courses.getCourses()) {
			CourseProgressSummaryResponse summary = getCourseProgressSummary(course.getCourseId(), studentId); // String
																												// ids
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
		response.setStudentName(student.getFirstNm() + " " + student.getLastNm()); // adjust to your Student fields
		response.setCourses(courses);
		response.setWeeklySchedule(weeklySchedule);
		response.setOverallProgress(overallProgress);

		return response;
	}

}
