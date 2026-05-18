package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.request.StudentTaskUpdateRequest;
import com.dmantz.lms.dto.response.HoursSpentResponse;
import com.dmantz.lms.dto.response.StudentTaskListResponse;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentNeedHelpRequest;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskStatus;
import com.dmantz.lms.entity.Topic;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTaskMapper;
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

	public StudentTaskServiceImpl(StudentTaskRepository studentTaskRepository, StudentRepository studentRepository,
			TopicRepository topicRepository, StudentTaskMapper studentTaskMapper) {

		this.studentTaskRepository = studentTaskRepository;
		this.studentRepository = studentRepository;
		this.topicRepository = topicRepository;
		this.studentTaskMapper = studentTaskMapper;
	}

	@Override
	public StudentTaskResponse addTask(StudentTaskRequest request) {

		logger.info("Adding student task for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		Student student = studentRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> {

			logger.error("Student not found with studentId: {}", request.getStudentId());

			return new ResourceNotFoundException("Student not found: " + request.getStudentId());
		});

		Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow(() -> {

			logger.error("Topic not found with topicId: {}", request.getTopicId());

			return new ResourceNotFoundException("Topic not found: " + request.getTopicId());
		});

		StudentTask task = studentTaskRepository
				.findByStudent_StudentIdAndTopic_Id(request.getStudentId(), request.getTopicId()).orElseGet(() -> {

					logger.info("Creating new task for studentId: {} and topicId: {}", request.getStudentId(),
							request.getTopicId());

					StudentTask newTask = new StudentTask();
					newTask.setStudent(student);
					newTask.setTopic(topic);
					newTask.setStartDt(LocalDateTime.now());
					newTask.setStatus(StudentTaskStatus.NOT_STARTED);
					newTask.setNeedHelp(false);

					return studentTaskRepository.save(newTask);
				});

		logger.info("Student task processed successfully for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		return studentTaskMapper.toResponse(task);
	}

	@Override
	public StudentTaskResponse updateNeedHelp(StudentNeedHelpRequest request) {

		logger.info("Updating need-help status for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		StudentTask task = studentTaskRepository
				.findByStudent_StudentIdAndTopic_Id(request.getStudentId(), request.getTopicId()).orElseThrow(() -> {

					logger.error("Task not found for studentId: {} and topicId: {}", request.getStudentId(),
							request.getTopicId());

					return new ResourceNotFoundException("Task not found for student: " + request.getStudentId()
							+ " and topic: " + request.getTopicId());
				});

		task.setNeedHelp(request.getNeedHelp());

		StudentTask updatedTask = studentTaskRepository.save(task);

		logger.info("Need-help status updated successfully for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		return studentTaskMapper.toResponse(updatedTask);
	}
	
	@Override
	public HoursSpentResponse getHoursSpent(String studentId) {
	    logger.info("Fetching hours spent for studentId: {}", studentId);

	    // Check student exists
	    if (!studentRepository.existsByStudentId(studentId)) {
	        logger.error("Student not found for studentId: {}", studentId);
	        throw new ResourceNotFoundException("Student not found with studentId: " + studentId);
	    }

	    Integer totalHours = studentTaskRepository.getTotalHoursSpent(studentId);
	    int hours = totalHours != null ? totalHours : 0;

	    logger.info("Total hours spent for studentId: {} is {}", studentId, hours);
	    return new HoursSpentResponse(hours);
	}
	
	
	@Override
	public StudentTaskResponse updateTask(StudentTaskUpdateRequest request) {

		logger.info("Updating student task for studentId: {} and topicId: {}", request.getStudentId(),
				request.getTopicId());

		StudentTask task = studentTaskRepository
				.findByStudent_StudentIdAndTopic_Id(request.getStudentId(), request.getTopicId()).orElseThrow(() -> {

					logger.error("Student task not found for studentId: {} and topicId: {}", request.getStudentId(),
							request.getTopicId());

					return new ResourceNotFoundException("Student task not found");
				});

		studentTaskMapper.updateTaskFromRequest(request, task);

		// Business Logic
		if (request.getStatus() == StudentTaskStatus.COMPLETED) {
			task.setEndDt(LocalDateTime.now());
		}

		StudentTask updatedTask = studentTaskRepository.save(task);

		logger.info("Student task updated successfully");

		return studentTaskMapper.toResponse(updatedTask);
	}

	@Override
	public String deleteTask(String studentId, Long topicId) {

		logger.info("Deleting student task for studentId: {} and topicId: {}", studentId, topicId);

		StudentTask task = studentTaskRepository.findByStudent_StudentIdAndTopic_Id(studentId, topicId)
				.orElseThrow(() -> {

					logger.error("Student task not found for studentId: {} and topicId: {}", studentId, topicId);

					return new ResourceNotFoundException("Student task not found");
				});

		studentTaskRepository.delete(task);

		logger.info("Student task deleted successfully for studentId: {} and topicId: {}", studentId, topicId);

		return "Student task deleted successfully";
	}

	@Override
	public StudentTaskListResponse getStudentTasks(String studentId, String statusFilter) {

		logger.info("Fetching tasks for studentId: {} with statusFilter: {}", studentId, statusFilter);

		if (statusFilter != null && !statusFilter.equalsIgnoreCase("ACTIVE")
				&& !statusFilter.equalsIgnoreCase("COMPLETED")) {

			logger.error("Invalid statusFilter: {} provided for studentId: {}", statusFilter, studentId);

			throw new IllegalArgumentException("Invalid status filter. Allowed values: ACTIVE, COMPLETED");
		}

		List<StudentTask> tasks;
		int count;

		if (statusFilter == null || statusFilter.equalsIgnoreCase("ACTIVE")) {

			logger.info("Fetching all tasks and completed tasks for studentId: {}", studentId);

			List<StudentTask> allTasks = studentTaskRepository.findByStudent_StudentId(studentId);

			List<StudentTask> completedTasks = studentTaskRepository.findByStudent_StudentIdAndStatus(studentId,
					StudentTaskStatus.COMPLETED);

			logger.info("Total tasks: {} | Completed tasks: {} for studentId: {}", allTasks.size(),
					completedTasks.size(), studentId);

			tasks = allTasks.stream().filter(task -> task.getStatus() != StudentTaskStatus.COMPLETED)
					.collect(Collectors.toList());

			count = allTasks.size() - completedTasks.size();

			logger.info("Active tasks count: {} for studentId: {}", count, studentId);

		} else {

			logger.info("Fetching COMPLETED tasks for studentId: {}", studentId);

			tasks = studentTaskRepository.findByStudent_StudentIdAndStatus(studentId, StudentTaskStatus.COMPLETED);

			count = tasks.size();

			logger.info("Completed tasks count: {} for studentId: {}", count, studentId);
		}

		StudentTaskListResponse response = new StudentTaskListResponse();
		response.setCount(count);
		response.setTasks(tasks.stream().map(studentTaskMapper::toResponse).collect(Collectors.toList()));

		logger.info("Successfully fetched {} tasks for studentId: {}", count, studentId);

		return response;
	}

	@Override
	public StudentTaskResponse markTaskCompleted(Long taskId, String studentId) {

		logger.info("Marking task as completed for studentId: {} and taskId: {}", studentId, taskId);

		studentRepository.findByStudentId(studentId).orElseThrow(() -> {

			logger.error("Student not found with studentId: {}", studentId);

			return new ResourceNotFoundException("Student not found: " + studentId);
		});

		StudentTask task = studentTaskRepository.findById(taskId)
				.filter(t -> t.getStudent().getStudentId().equals(studentId)).orElseThrow(() -> {

					logger.error("Task not found with taskId: {} for studentId: {}", taskId, studentId);

					return new ResourceNotFoundException(
							"Task not found with id: " + taskId + " for student: " + studentId);
				});

		if (task.getStatus() == StudentTaskStatus.COMPLETED) {

			logger.warn("Task with taskId: {} is already completed for studentId: {}", taskId, studentId);

			throw new IllegalStateException("Task is already marked as completed");
		}

		task.setStatus(StudentTaskStatus.COMPLETED);
		task.setEndDt(LocalDateTime.now());

		StudentTask updatedTask = studentTaskRepository.save(task);

		logger.info("Task marked as completed successfully for studentId: {} and taskId: {}", studentId, taskId);

		return studentTaskMapper.toResponse(updatedTask);
	}


}