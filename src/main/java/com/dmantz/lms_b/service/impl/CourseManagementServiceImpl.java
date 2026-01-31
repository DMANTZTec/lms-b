package com.dmantz.lms_b.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.entity.Subject;
import com.dmantz.lms_b.exceptions.DuplicateValuesException;
import com.dmantz.lms_b.exceptions.ResourceNotFoundException;
import com.dmantz.lms_b.mapper.SubjectMapper;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.repository.SubjectRepository;
import com.dmantz.lms_b.service.CourseManagementService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseManagementServiceImpl implements CourseManagementService {

    private final SubjectRepository subjectRepository;
    private final StaffRepository staffRepository;
    private final SubjectMapper subjectMapper;

    public CourseManagementServiceImpl(
            SubjectRepository subjectRepository,
            StaffRepository staffRepository,
            SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.staffRepository = staffRepository;
        this.subjectMapper = subjectMapper;
    }

    // ------------------ CREATE SUBJECT ------------------
    @Override
    public SubjectResponse createSubject(SubjectRequest requestDto, Long staffId) {

        // Validate staff
        if (!staffRepository.existsById(staffId)) {
            throw new ResourceNotFoundException(
                    "Staff with ID " + staffId + " does not exist"
            );
        }

        // Check duplicate short code
        subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd())
                .ifPresent(existing -> {
                    throw new DuplicateValuesException(
                            "Subject already exists with code: "
                                    + requestDto.getSubjectShortCd()
                    );
                });

        Subject subject = subjectMapper.toEntity(requestDto);
        subject.setCreated_by(staffId);
        subject.setCreated_dt(LocalDateTime.now());

        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(savedSubject);
    }

    // ------------------ VIEW ALL SUBJECTS ------------------
    @Override
    public List<SubjectResponse> viewAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    // ------------------ UPDATE SUBJECT ------------------
    @Override
    public SubjectResponse updateSubject(
            Long subjectId,
            SubjectRequest requestDto,
            Long staffId) {

        // Validate staff
        if (!staffRepository.existsById(staffId)) {
            throw new ResourceNotFoundException(
                    "Staff with ID " + staffId + " does not exist"
            );
        }

        // Fetch subject
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id: " + subjectId
                        )
                );

        // Check duplicate short code (excluding same subject)
        subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(subjectId)) {
                        throw new DuplicateValuesException(
                                "Another subject already exists with code: "
                                        + requestDto.getSubjectShortCd()
                        );
                    }
                });

        // Update fields using MapStruct
        subjectMapper.updateSubjectFromRequest(requestDto, subject);

        subject.setUpdated_by(staffId);
        subject.setUpdated_dt(LocalDateTime.now());

        Subject updatedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(updatedSubject);
    }

    // ------------------ DELETE SUBJECT ------------------
    @Override
    public SubjectResponse deleteSubject(Long subjectId, Long staffId) {

        // Validate staff
        if (!staffRepository.existsById(staffId)) {
            throw new ResourceNotFoundException(
                    "Staff with ID " + staffId + " does not exist"
            );
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id: " + subjectId
                        )
                );

        // Audit before delete (optional but good)
        subject.setUpdated_by(staffId);
        subject.setUpdated_dt(LocalDateTime.now());

        subjectRepository.delete(subject);

        return subjectMapper.toDto(subject);
    }
}
