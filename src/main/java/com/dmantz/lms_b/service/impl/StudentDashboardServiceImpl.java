package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.ChapterProgressResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.StudentMyCoursesResponse;
import com.dmantz.lms_b.dto.response.TopicProgressResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms_b.entity.*;
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

	private final CourseRepository courseRepository;
	private final StudentTopicReferenceProgressRepository progressRepository;

	public StudentDashboardServiceImpl(ClassScheduleRepository classScheduleRepository,
			ClassScheduleMapper classScheduleMapper, ClassBatchRepository classBatchRepository,
			StaffRepository staffRepository, StudentCourseRepository studentCourseRepository,
			StudentCourseMapper studentCourseMapper, CourseRepository courseRepository,
			StudentTopicReferenceProgressRepository progressRepository) {
		super();
		this.classScheduleRepository = classScheduleRepository;
		this.classScheduleMapper = classScheduleMapper;
		this.classBatchRepository = classBatchRepository;
		this.staffRepository = staffRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.studentCourseMapper = studentCourseMapper;
		this.courseRepository = courseRepository;
		this.progressRepository = progressRepository;
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

		// fetch all for counts
		List<StudentCourse> allCourses = studentCourseRepository.findByStudentStudentId(studentId);

		// fetch filtered list for tab
		List<StudentCourse> filteredCourses = (status == null) ? allCourses
				: studentCourseRepository.findByStudentStudentIdAndStatus(studentId, status);

		StudentMyCoursesResponse response = new StudentMyCoursesResponse();

		// counts
		response.setTotalCourses(allCourses.size());
		response.setOngoing(countByStatus(allCourses, CourseStatus.ONGOING));
		response.setPlanned(countByStatus(allCourses, CourseStatus.PLANNED));
		response.setCompleted(countByStatus(allCourses, CourseStatus.COMPLETED));

		// course list
		response.setCourses(filteredCourses.stream().map(studentCourseMapper::toDto).toList());

		return response;
	}

	private long countByStatus(List<StudentCourse> list, CourseStatus status) {
		return list.stream().filter(c -> c.getStatus() == status).count();
	}

	@Override
	public List<TopicProgressResponse> getTopicProgress(Long courseId, Long studentId) {

		Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

		List<TopicProgressResponse> response = new ArrayList<>();

		for (Chapter chapter : course.getChapters()) {
			for (Topic topic : chapter.getTopics()) {

				List<TopicReference> references = topic.getReferences();
				int totalReferences = (references == null) ? 0 : references.size();

				// all progress rows for this student on this topic
				List<StudentTopicReferenceProgress> studentProgress = progressRepository
						.findByStudent_IdAndTopicReference_Topic_Id(studentId, topic.getId());

				Set<Long> completedReferenceIds = studentProgress.stream()
						.filter(p -> Boolean.TRUE.equals(p.getCompleted())).map(p -> p.getTopicReference().getId())
						.collect(java.util.stream.Collectors.toSet());

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
	public List<ChapterProgressResponse> getChapterProgress(Long courseId, Long studentId) {

		Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

		// Get all topic progress
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
}

//    @Override
//    public StudentDashboardResponse getDashboard(String studentId) {
//        List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);
//        return studentCourseMapper.toDashboard(studentCourses);
//    }
