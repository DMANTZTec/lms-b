package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.StudentTopicReferenceProgressRequest;
import com.dmantz.lms_b.dto.response.StudentTopicReferenceProgressResponse;
import com.dmantz.lms_b.entity.Student;
import com.dmantz.lms_b.entity.StudentTopicReferenceProgress;
import com.dmantz.lms_b.entity.TopicReference;
import com.dmantz.lms_b.exceptions.ResourceNotFoundException;
import com.dmantz.lms_b.mapper.StudentTopicReferenceProgressMapper;
import com.dmantz.lms_b.repository.StudentRepository;
import com.dmantz.lms_b.repository.StudentTopicReferenceProgressRepository;
import com.dmantz.lms_b.repository.TopicReferenceRepository;
import com.dmantz.lms_b.service.StudentTopicReferenceProgressService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StudentTopicReferenceProgressServiceImpl implements StudentTopicReferenceProgressService {
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

		Student student = studentRepository.findByStudentId(request.getStudentId())
				.orElseThrow(() -> new RuntimeException("Student not found: " + request.getStudentId()));

		TopicReference topicReference = topicReferenceRepository.findById(request.getReferenceId())
				.orElseThrow(() -> new RuntimeException("Reference not found: " + request.getReferenceId()));

		Optional<StudentTopicReferenceProgress> existing = progressRepository.findByStudent_IdAndTopicReference_Id(
				student.getId(), // ← Long internal id
				topicReference.getId() // ← Long internal id
		);

		StudentTopicReferenceProgress progress = existing.orElseGet(StudentTopicReferenceProgress::new);
		progress.setStudent(student);
		progress.setTopicReference(topicReference);
		progress.setCompleted(true);
		progress.setCompletedAt(LocalDateTime.now());

		StudentTopicReferenceProgress saved = progressRepository.save(progress);
		return studentTopicReferenceProgressmapper.toResponse(saved); // response will have String studentId
	}
}