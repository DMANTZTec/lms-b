package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.request.StudentTaskUpdateRequest;
import com.dmantz.lms.dto.response.ChapterDropdownResponse;
import com.dmantz.lms.dto.response.CourseDropdownResponse;
import com.dmantz.lms.dto.response.HoursSpentResponse;
import com.dmantz.lms.dto.response.StudentTaskListResponse;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.dto.response.TopicDropdownResponse;
import com.dmantz.lms.entity.Chapter;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentNeedHelpRequest;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskStatus;
import com.dmantz.lms.entity.Topic;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTaskMapper;
import com.dmantz.lms.repository.ChapterRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.StudentCourseRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.repository.StudentTaskRepository;
import com.dmantz.lms.repository.TopicRepository;
import com.dmantz.lms.service.StudentTaskService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentTaskServiceImpl implements StudentTaskService {

	private static final Logger logger = LogManager.getLogger(StudentTaskServiceImpl.class);

	private final StudentTaskRepository studentTaskRepository;
	private final StudentRepository studentRepository;
	private final TopicRepository topicRepository;
	private final StudentTaskMapper studentTaskMapper;
	private final StudentCourseRepository studentCourseRepository;
	private final CourseRepository courseRepository;
	private final ChapterRepository chapterRepository;
	private final EnrollmentRepository enrollmentRepository;

	

	public StudentTaskServiceImpl(StudentTaskRepository studentTaskRepository, StudentRepository studentRepository,
			TopicRepository topicRepository, StudentTaskMapper studentTaskMapper,
			StudentCourseRepository studentCourseRepository, CourseRepository courseRepository,
			ChapterRepository chapterRepository, EnrollmentRepository enrollmentRepository) {
		super();
		this.studentTaskRepository = studentTaskRepository;
		this.studentRepository = studentRepository;
		this.topicRepository = topicRepository;
		this.studentTaskMapper = studentTaskMapper;
		this.studentCourseRepository = studentCourseRepository;
		this.courseRepository = courseRepository;
		this.chapterRepository = chapterRepository;
		this.enrollmentRepository = enrollmentRepository;
	}

	@Override
	public StudentTaskResponse addTask(StudentTaskRequest request) {

	    logger.info("Creating task for studentId: {} courseId: {}", request.getStudentId(), request.getCourseId());

	    Student student = studentRepository.findByStudentId(request.getStudentId())
	            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

	    boolean isEnrolled = enrollmentRepository
	            .existsByStudentStudentIdAndCourseCourseId(
	                    request.getStudentId(),
	                    request.getCourseId()
	            );

	    if (!isEnrolled) {
	        throw new IllegalStateException(
	                "Student is not actively enrolled in the selected course"
	        );
	    }
	    if (!isEnrolled) {
	        throw new IllegalStateException("Student is not enrolled in the selected course");
	    }

	    Course course = courseRepository.findByCourseId(request.getCourseId())
	            .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

	    Chapter chapter = null;
	    if (request.getChapterId() != null) {
	        chapter = chapterRepository.findById(request.getChapterId())
	                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found: " + request.getChapterId()));

	        if (!chapter.getCourse().getCourseId().equals(course.getCourseId())) {
	            throw new IllegalArgumentException("Selected chapter does not belong to the selected course");
	        }
	    }

	    Topic topic = null;
	    if (request.getTopicId() != null) {
	        topic = topicRepository.findById(request.getTopicId())
	                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + request.getTopicId()));

	        if (chapter == null || !topic.getChapter().getId().equals(chapter.getId())) {
	            throw new IllegalArgumentException("Selected topic does not belong to the selected chapter");
	        }
	    }

	    StudentTask task = studentTaskMapper.toEntity(request);

	    task.setCourse(course);
	    task.setChapter(chapter);
	    task.setTopic(topic);
	    task.setStudent(student);
	    task.setStatus(StudentTaskStatus.ACTIVE);
	    task.setStartDt(LocalDateTime.now());
	    task.setNeedHelp(false);

	    StudentTask saved = studentTaskRepository.save(task);

	    logger.info("Task created successfully with id: {}", saved.getId());

	    return studentTaskMapper.toResponse(saved);
	}

	@Override
	public List<CourseDropdownResponse> getEnrolledCourses(String studentId) {

	    logger.info("Fetching enrolled courses for studentId: {}", studentId);

	    studentRepository.findByStudentId(studentId)
	            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

	    return studentCourseRepository.findByStudent_StudentId(studentId).stream()
	            .map(sc -> new CourseDropdownResponse(sc.getCourse().getCourseId(), sc.getCourse().getCourseTitle()))
	            .toList();
	}

	@Override
	public List<ChapterDropdownResponse> getChaptersByCourse(String courseId) {

	    logger.info("Fetching chapters for courseId: {}", courseId);

	    return chapterRepository.findByCourse_CourseId(courseId).stream()
	            .map(c -> new ChapterDropdownResponse(c.getId(), c.getChapterNm()))
	            .toList();
	}

	@Override
	public List<TopicDropdownResponse> getTopicsByChapter(Long chapterId) {

	    logger.info("Fetching topics for chapterId: {}", chapterId);

	    return topicRepository.findByChapter_Id(chapterId).stream()
	            .map(t -> new TopicDropdownResponse(t.getId(), t.getTopicNm()))
	            .toList();
	}
	
	@Override
	public StudentTaskListResponse getTasksByStatus(String studentId, String statusFilter) {

	    logger.info("Fetching tasks for studentId: {} filtered by status: {}", studentId, statusFilter);

	    studentRepository.findByStudentId(studentId)
	            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

	    if (!"ACTIVE".equalsIgnoreCase(statusFilter) && !"COMPLETED".equalsIgnoreCase(statusFilter)) {
	        throw new IllegalArgumentException("Invalid status filter. Allowed values: ACTIVE, COMPLETED");
	    }

	    List<StudentTask> tasks;

	    if ("COMPLETED".equalsIgnoreCase(statusFilter)) {
	        tasks = studentTaskRepository.findByStudent_StudentIdAndStatus(studentId, StudentTaskStatus.COMPLETED);
	    } else {
	        // ACTIVE = everything not yet completed (NOT_STARTED, IN_PROGRESS, SUBMITTED, REVIEWED)
	        tasks = studentTaskRepository.findByStudent_StudentId(studentId).stream()
	                .filter(t -> t.getStatus() != StudentTaskStatus.COMPLETED)
	                .toList();
	    }

	    StudentTaskListResponse response = new StudentTaskListResponse();
	    response.setCount(tasks.size());
	    response.setTasks(tasks.stream().map(studentTaskMapper::toResponse).toList());

	    return response;
	}
	

//	@Override
//	public StudentTaskResponse updateNeedHelp(StudentNeedHelpRequest request) {
//
//		logger.info("Updating need-help status for studentId: {} and topicId: {}", request.getStudentId(),
//				request.getTopicId());
//
//		StudentTask task = studentTaskRepository
//				.findByStudent_StudentIdAndTopic_Id(request.getStudentId(), request.getTopicId()).orElseThrow(() -> {
//
//					logger.error("Task not found for studentId: {} and topicId: {}", request.getStudentId(),
//							request.getTopicId());
//
//					return new ResourceNotFoundException("Task not found for student: " + request.getStudentId()
//							+ " and topic: " + request.getTopicId());
//				});
//
//		task.setNeedHelp(request.getNeedHelp());
//
//		StudentTask updatedTask = studentTaskRepository.save(task);
//
//		logger.info("Need-help status updated successfully for studentId: {} and topicId: {}", request.getStudentId(),
//				request.getTopicId());
//
//		return studentTaskMapper.toResponse(updatedTask);
//	}
//	
//	@Override
//	public HoursSpentResponse getHoursSpent(String studentId) {
//	    logger.info("Fetching hours spent for studentId: {}", studentId);
//
//	    // Check student exists
//	    if (!studentRepository.existsByStudentId(studentId)) {
//	        logger.error("Student not found for studentId: {}", studentId);
//	        throw new ResourceNotFoundException("Student not found with studentId: " + studentId);
//	    }
//
//	    Integer totalHours = studentTaskRepository.getTotalHoursSpent(studentId);
//	    int hours = totalHours != null ? totalHours : 0;
//
//	    logger.info("Total hours spent for studentId: {} is {}", studentId, hours);
//	    return new HoursSpentResponse(hours);
//	}
//	
//	
//	@Override
//	public StudentTaskResponse updateTask(StudentTaskUpdateRequest request) {
//
//		logger.info("Updating student task for studentId: {} and topicId: {}", request.getStudentId(),
//				request.getTopicId());
//
//		StudentTask task = studentTaskRepository
//				.findByStudent_StudentIdAndTopic_Id(request.getStudentId(), request.getTopicId()).orElseThrow(() -> {
//
//					logger.error("Student task not found for studentId: {} and topicId: {}", request.getStudentId(),
//							request.getTopicId());
//
//					return new ResourceNotFoundException("Student task not found");
//				});
//
//		studentTaskMapper.updateTaskFromRequest(request, task);
//
//		// Business Logic
//		if (request.getStatus() == StudentTaskStatus.COMPLETED) {
//			task.setEndDt(LocalDateTime.now());
//		}
//
//		StudentTask updatedTask = studentTaskRepository.save(task);
//
//		logger.info("Student task updated successfully");
//
//		return studentTaskMapper.toResponse(updatedTask);
//	}
//
//	@Override
//	public String deleteTask(String studentId, Long topicId) {
//
//		logger.info("Deleting student task for studentId: {} and topicId: {}", studentId, topicId);
//
//		StudentTask task = studentTaskRepository.findByStudent_StudentIdAndTopic_Id(studentId, topicId)
//				.orElseThrow(() -> {
//
//					logger.error("Student task not found for studentId: {} and topicId: {}", studentId, topicId);
//
//					return new ResourceNotFoundException("Student task not found");
//				});
//
//		studentTaskRepository.delete(task);
//
//		logger.info("Student task deleted successfully for studentId: {} and topicId: {}", studentId, topicId);
//
//		return "Student task deleted successfully";
//	}
//
//	@Override
//	public StudentTaskListResponse getStudentTasks(String studentId, String statusFilter) {
//
//		logger.info("Fetching tasks for studentId: {} with statusFilter: {}", studentId, statusFilter);
//
//		if (statusFilter != null && !statusFilter.equalsIgnoreCase("ACTIVE")
//				&& !statusFilter.equalsIgnoreCase("COMPLETED")) {
//
//			logger.error("Invalid statusFilter: {} provided for studentId: {}", statusFilter, studentId);
//
//			throw new IllegalArgumentException("Invalid status filter. Allowed values: ACTIVE, COMPLETED");
//		}
//
//		List<StudentTask> tasks;
//		int count;
//
//		if (statusFilter == null || statusFilter.equalsIgnoreCase("ACTIVE")) {
//
//			logger.info("Fetching all tasks and completed tasks for studentId: {}", studentId);
//
//			List<StudentTask> allTasks = studentTaskRepository.findByStudent_StudentId(studentId);
//
//			List<StudentTask> completedTasks = studentTaskRepository.findByStudent_StudentIdAndStatus(studentId,
//					StudentTaskStatus.COMPLETED);
//
//			logger.info("Total tasks: {} | Completed tasks: {} for studentId: {}", allTasks.size(),
//					completedTasks.size(), studentId);
//
//			tasks = allTasks.stream().filter(task -> task.getStatus() != StudentTaskStatus.COMPLETED)
//					.collect(Collectors.toList());
//
//			count = allTasks.size() - completedTasks.size();
//
//			logger.info("Active tasks count: {} for studentId: {}", count, studentId);
//
//		} else {
//
//			logger.info("Fetching COMPLETED tasks for studentId: {}", studentId);
//
//			tasks = studentTaskRepository.findByStudent_StudentIdAndStatus(studentId, StudentTaskStatus.COMPLETED);
//
//			count = tasks.size();
//
//			logger.info("Completed tasks count: {} for studentId: {}", count, studentId);
//		}
//
//		StudentTaskListResponse response = new StudentTaskListResponse();
//		response.setCount(count);
//		response.setTasks(tasks.stream().map(studentTaskMapper::toResponse).collect(Collectors.toList()));
//
//		logger.info("Successfully fetched {} tasks for studentId: {}", count, studentId);
//
//		return response;
//	}
//
//	@Override
//	public StudentTaskResponse markTaskCompleted(Long taskId, String studentId) {
//
//		logger.info("Marking task as completed for studentId: {} and taskId: {}", studentId, taskId);
//
//		studentRepository.findByStudentId(studentId).orElseThrow(() -> {
//
//			logger.error("Student not found with studentId: {}", studentId);
//
//			return new ResourceNotFoundException("Student not found: " + studentId);
//		});
//
//		StudentTask task = studentTaskRepository.findById(taskId)
//				.filter(t -> t.getStudent().getStudentId().equals(studentId)).orElseThrow(() -> {
//
//					logger.error("Task not found with taskId: {} for studentId: {}", taskId, studentId);
//
//					return new ResourceNotFoundException(
//							"Task not found with id: " + taskId + " for student: " + studentId);
//				});
//
//		if (task.getStatus() == StudentTaskStatus.COMPLETED) {
//
//			logger.warn("Task with taskId: {} is already completed for studentId: {}", taskId, studentId);
//
//			throw new IllegalStateException("Task is already marked as completed");
//		}
//
//		task.setStatus(StudentTaskStatus.COMPLETED);
//		task.setEndDt(LocalDateTime.now());
//
//		StudentTask updatedTask = studentTaskRepository.save(task);
//
//		logger.info("Task marked as completed successfully for studentId: {} and taskId: {}", studentId, taskId);
//
//		return studentTaskMapper.toResponse(updatedTask);
//	}


}