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
	public StudentTopicReferenceProgressResponse markReferenceCompleted(StudentTopicReferenceProgressRequest request) {

		Long studentId = request.getStudentId();
		Long referenceId = request.getReferenceId();

		StudentTopicReferenceProgress progress = progressRepository
				.findByStudent_IdAndTopicReference_Id(studentId, referenceId).orElseGet(() -> {

					Student student = studentRepository.findById(studentId)
							.orElseThrow(() -> new ResourceNotFoundException("Student not found with id "+studentId ));

					TopicReference reference = topicReferenceRepository.findById(referenceId)
							.orElseThrow(() -> new ResourceNotFoundException("Reference not found with id + "+referenceId));

					StudentTopicReferenceProgress newProgress = new StudentTopicReferenceProgress();

					newProgress.setStudent(student);
					newProgress.setTopicReference(reference);
					newProgress.setCompleted(false);

					return newProgress;
				});


		if (Boolean.TRUE.equals(progress.getCompleted())) {
			return studentTopicReferenceProgressmapper.toResponse(progress);
		}

		progress.setCompleted(true);
		progress.setCompletedAt(LocalDateTime.now());

		StudentTopicReferenceProgress saved = progressRepository.save(progress);

		return studentTopicReferenceProgressmapper.toResponse(saved);
	}

}