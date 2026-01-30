package com.dmantz.lms_b.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.entity.Subject;
import com.dmantz.lms_b.exceptions.DuplicateValuesException;
import com.dmantz.lms_b.exceptions.ResourceNotFoundException;
import com.dmantz.lms_b.mapper.SubjectMapper;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.repository.SubjectRepository;
import com.dmantz.lms_b.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

	private final SubjectRepository subjectrepository;

	private final StaffRepository staffrepository;

	private final SubjectMapper subjectmapper;

	public SubjectServiceImpl(SubjectRepository subjectrepository, StaffRepository staffrepository,
			SubjectMapper subjectmapper) {
		super();
		this.subjectrepository = subjectrepository;
		this.staffrepository = staffrepository;
		this.subjectmapper = subjectmapper;
	}

	@Override
	public SubjectResponse createSubject(SubjectRequest requestDto, Long staffID) {

		boolean staffExists = staffrepository.existsById(staffID);
		if (!staffExists) {
			throw new ResourceNotFoundException("Staff with ID " + staffID + " does not exist");
		}

		subjectrepository.findBySubjectShortCd(requestDto.getSubject_short_cd()).ifPresent(s -> {
			throw new DuplicateValuesException("Subject already exists with code: " + requestDto.getSubject_short_cd());
		});

		Subject subject = subjectmapper.toEntity(requestDto);

		subject.setCreated_by(staffID);
		subject.setCreated_dt(LocalDateTime.now());

		Subject savedSubject = subjectrepository.save(subject);

		return subjectmapper.toDto(savedSubject);
	}

	@Override
	public List<SubjectResponse> getAllSubjects() {

		List<Subject> subjects = subjectrepository.findAll();

		return subjects.stream().map(subjectmapper::toDto).collect(Collectors.toList());
	}

}