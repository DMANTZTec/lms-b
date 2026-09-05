package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.StudentTaskMentorRequest;
import com.dmantz.lms.dto.request.UpdateMentorMinutesRequest;
import com.dmantz.lms.dto.response.MentorPointsResponse;
import com.dmantz.lms.dto.response.StudentTaskMentorResponse;
import com.dmantz.lms.entity.MentorHelpStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskMentor;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTaskMentorMapper;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.repository.StudentTaskMentorRepository;
import com.dmantz.lms.repository.StudentTaskRepository;
import com.dmantz.lms.service.StudentTaskMentorService;

@Service
public class StudentTaskMentorServiceImpl implements StudentTaskMentorService {

	private static final Logger logger = LogManager.getLogger(StudentTaskMentorServiceImpl.class);

	@Autowired
	private StudentTaskMentorRepository mentorRepo;

	@Autowired
	private StudentTaskRepository taskRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private StudentTaskMentorMapper mapper;

	@Override
	public StudentTaskMentorResponse createMentoringActivity(StudentTaskMentorRequest request) {

		logger.info("Creating mentoring activity for studentTaskId: {} and mentorStudentId: {}",
				request.getStudentTaskId(), request.getMentorStudentId());

		StudentTask task = taskRepo.findById(request.getStudentTaskId()).orElseThrow(() -> {

			logger.warn("StudentTask not found with id: {}", request.getStudentTaskId());

			return new ResourceNotFoundException("StudentTask not found with id: " + request.getStudentTaskId());
		});

		Student mentor = studentRepo.findByStudentId(request.getMentorStudentId()).orElseThrow(() -> {

			logger.warn("Mentor not found with studentId: {}", request.getMentorStudentId());

			return new ResourceNotFoundException("Mentor not found with studentId: " + request.getMentorStudentId());
		});

		boolean exists = mentorRepo.existsByStudentTask_IdAndMentorStudent_StudentId(request.getStudentTaskId(),
				request.getMentorStudentId());

		if (exists) {

			logger.warn("Mentoring activity already exists for taskId: {} and mentorStudentId: {}",
					request.getStudentTaskId(), request.getMentorStudentId());

			throw new DuplicateValuesException("Mentoring activity already exists");
		}

		StudentTaskMentor entity = mapper.toEntity(request);

		entity.setStudentTask(task);
		entity.setMentorStudent(mentor);
		entity.setStatus(MentorHelpStatus.IN_PROGRESS);

		StudentTaskMentor saved = mentorRepo.save(entity);

		logger.info("Mentoring activity created successfully with id: {}", saved.getId());

		return mapper.toDto(saved);
	}

	@Override
	public StudentTaskMentorResponse updateMentoringMinutes(Long id, UpdateMentorMinutesRequest request) {

		logger.info("Updating mentoring minutes for mentoringId: {}", id);

		StudentTaskMentor entity = mentorRepo.findById(id).orElseThrow(() -> {

			logger.warn("Mentoring record not found with id: {}", id);

			return new ResourceNotFoundException("Mentoring record not found with id: " + id);
		});

		entity.setMinsSpent(request.getMinsSpent());
		entity.setStatus(MentorHelpStatus.IN_PROGRESS);

		StudentTaskMentor updated = mentorRepo.save(entity);

		logger.info("Mentoring minutes updated successfully for id: {}", updated.getId());

		return mapper.toDto(updated);
	}

	@Override
	public StudentTaskMentorResponse acknowledgeMentorHelp(Long id) {

		logger.info("Acknowledging mentor help for mentoringId: {}", id);

		StudentTaskMentor entity = mentorRepo.findById(id).orElseThrow(() -> {

			logger.warn("Mentoring record not found with id: {}", id);

			return new ResourceNotFoundException("Mentoring record not found with id: " + id);
		});

		if (Boolean.TRUE.equals(entity.getStudentAck())) {

			logger.warn("Mentor help already acknowledged for id: {}", id);

			throw new DuplicateValuesException("Mentor help already acknowledged");
		}

		entity.setStudentAck(true);
		entity.setStatus(MentorHelpStatus.COMPLETED);

		StudentTaskMentor updated = mentorRepo.save(entity);

		logger.info("Mentor help acknowledged successfully for id: {}", updated.getId());

		return mapper.toDto(updated);
	}
	@Override
	public MentorPointsResponse getMentorPointsSummary(String studentId) {
	    logger.info("Fetching mentor points summary for studentId: {}", studentId);

	    // Check if student exists
	    if (!studentRepo.existsByStudentId(studentId)) {
	        logger.error("Student not found for studentId: {}", studentId);
	        throw new ResourceNotFoundException("Student not found with studentId: " + studentId);
	    }

	    Integer total = mentorRepo.getTotalPoints(studentId);
	    Integer thisMonth = mentorRepo.getPointsSince(
	            studentId,
	            LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
	    );

	    MentorPointsResponse response = new MentorPointsResponse();
	    response.setTotalPoints(total != null ? total : 0);
	    response.setThisMonthPoints(thisMonth != null ? thisMonth : 0);

	    logger.info("Mentor points for studentId: {} — total: {}, thisMonth: {}", studentId, total, thisMonth);
	    return response;
	}}