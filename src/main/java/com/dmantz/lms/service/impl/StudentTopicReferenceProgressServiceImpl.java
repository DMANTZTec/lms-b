package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms.dto.response.StudentTopicReferenceProgressResponse;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentTopicReferenceProgress;
import com.dmantz.lms.entity.TopicReference;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTopicReferenceProgressMapper;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.repository.StudentTopicReferenceProgressRepository;
import com.dmantz.lms.repository.TopicReferenceRepository;
import com.dmantz.lms.service.StudentTopicReferenceProgressService;

import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class StudentTopicReferenceProgressServiceImpl implements StudentTopicReferenceProgressService {

	private static final Logger logger = LogManager.getLogger(StudentTopicReferenceProgressServiceImpl.class);

	private final StudentTopicReferenceProgressRepository progressRepository;

	private final TopicReferenceRepository topicReferenceRepository;

	private final StudentRepository studentRepository;

	private final StudentTopicReferenceProgressMapper studentTopicReferenceProgressmapper;

	public StudentTopicReferenceProgressServiceImpl(StudentTopicReferenceProgressRepository progressRepository,
			TopicReferenceRepository topicReferenceRepository, StudentRepository studentRepository,
			StudentTopicReferenceProgressMapper studentTopicReferenceProgressmapper) {

		this.progressRepository = progressRepository;
		this.topicReferenceRepository = topicReferenceRepository;
		this.studentRepository = studentRepository;
		this.studentTopicReferenceProgressmapper = studentTopicReferenceProgressmapper;
	}

	@Override
	public StudentTopicReferenceProgressResponse markReferenceComplete(StudentTopicReferenceProgressRequest request) {

		logger.info("Mark reference complete request received for studentId: {} and referenceId: {}",
				request.getStudentId(), request.getReferenceId());

		Student student = studentRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> {

			logger.error("Student not found with studentId: {}", request.getStudentId());

			return new ResourceNotFoundException("Student not found: " + request.getStudentId());
		});

		TopicReference topicReference = topicReferenceRepository.findById(request.getReferenceId()).orElseThrow(() -> {

			logger.error("Topic reference not found with referenceId: {}", request.getReferenceId());

			return new ResourceNotFoundException("Reference not found: " + request.getReferenceId());
		});

		Optional<StudentTopicReferenceProgress> existing = progressRepository
				.findByStudent_IdAndTopicReference_Id(student.getId(), topicReference.getId());

		// already completed validation
		if (existing.isPresent() && Boolean.TRUE.equals(existing.get().getCompleted())) {

			logger.error("Reference already completed for studentId: {} and referenceId: {}", request.getStudentId(),
					request.getReferenceId());

			throw new DuplicateValuesException("Reference already completed by student");
		}

		StudentTopicReferenceProgress progress = existing.orElseGet(StudentTopicReferenceProgress::new);

		progress.setStudent(student);
		progress.setTopicReference(topicReference);
		progress.setCompleted(true);
		progress.setCompletedAt(LocalDateTime.now());

		StudentTopicReferenceProgress saved = progressRepository.save(progress);

		logger.info("Reference marked completed successfully for studentId: {} and referenceId: {}",
				request.getStudentId(), request.getReferenceId());

		return studentTopicReferenceProgressmapper.toResponse(saved);
	}
}