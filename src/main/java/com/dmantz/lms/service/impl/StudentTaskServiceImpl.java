package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.response.HoursSpentResponse;
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
}