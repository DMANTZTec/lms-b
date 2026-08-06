package com.dmantz.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.SuccessStoryRequest;
import com.dmantz.lms.dto.response.SuccessStoryResponse;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.SuccessStory;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.SuccessStoryMapper;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.repository.SuccessStoryRepository;
import com.dmantz.lms.service.SuccessStoryService;

@Service
public class SuccessStoryServiceImpl implements SuccessStoryService {

    @Autowired
    private SuccessStoryRepository successStoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SuccessStoryMapper successStoryMapper;

    @Override
    public SuccessStoryResponse create(SuccessStoryRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        SuccessStory entity = successStoryMapper.toEntity(request);
        entity.setStudent(student);
        entity.setIsActive(true);

        SuccessStory saved = successStoryRepository.save(entity);

        return successStoryMapper.toResponse(saved);
    }

    @Override
    public List<SuccessStoryResponse> getActiveStories() {
        List<SuccessStory> entities = successStoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return successStoryMapper.toResponseList(entities);
    }

    @Override
    public List<SuccessStoryResponse> getAllStories() {
        List<SuccessStory> entities = successStoryRepository.findAll();
        return successStoryMapper.toResponseList(entities);
    }

    @Override
    public SuccessStoryResponse update(Long id, SuccessStoryRequest request) {

        SuccessStory entity = successStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Success story not found: " + id));

        if (!entity.getStudent().getId().equals(request.getStudentId())) {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
            entity.setStudent(student);
        }

        entity.setPlacedCompany(request.getPlacedCompany());
        entity.setPlacedDesignation(request.getPlacedDesignation());
        entity.setReviewMsg(request.getReviewMsg());
        entity.setDisplayOrder(request.getDisplayOrder());

        SuccessStory updated = successStoryRepository.save(entity);

        return successStoryMapper.toResponse(updated);
    }

    @Override
    public void toggleActive(Long id) {
        SuccessStory entity = successStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Success story not found: " + id));

        entity.setIsActive(!entity.getIsActive());
        successStoryRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (!successStoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Success story not found: " + id);
        }
        successStoryRepository.deleteById(id);
    }

}