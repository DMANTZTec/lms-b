package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.LearnerPathRequest;
import com.dmantz.lms.dto.response.LearnerPathResponse;
import com.dmantz.lms.entity.LearnerPath;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.LearnerPathMapper;
import com.dmantz.lms.repository.LearnerPathRepository;
import com.dmantz.lms.service.LearnerPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearnerPathServiceImpl implements LearnerPathService {

	@Autowired
	private LearnerPathRepository learnerPathRepository;

	@Autowired
	private LearnerPathMapper learnerPathMapper;

	@Override
	public LearnerPathResponse create(LearnerPathRequest request) {
		LearnerPath entity = learnerPathMapper.toEntity(request);
		entity.setIsActive(true);

		LearnerPath saved = learnerPathRepository.save(entity);
		return learnerPathMapper.toResponse(saved);
	}

	@Override
	public List<LearnerPathResponse> getActivePaths() {
		List<LearnerPath> entities = learnerPathRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
		return learnerPathMapper.toResponseList(entities);
	}

	@Override
	public List<LearnerPathResponse> getAllPaths() {
		List<LearnerPath> entities = learnerPathRepository.findAll();
		return learnerPathMapper.toResponseList(entities);
	}

	@Override
	public LearnerPathResponse update(Long id, LearnerPathRequest request) {
		LearnerPath entity = learnerPathRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Learner path not found: " + id));

		entity.setIcon(request.getIcon());
		entity.setTitle(request.getTitle());
		entity.setDescription(request.getDescription());
		entity.setItems(learnerPathMapper.toJson(request.getItems()));
		entity.setDisplayOrder(request.getDisplayOrder());

		LearnerPath updated = learnerPathRepository.save(entity);
		return learnerPathMapper.toResponse(updated);
	}

	@Override
	public LearnerPathResponse getById(Long id) {

		LearnerPath entity = learnerPathRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Learner path not found: " + id));

		return learnerPathMapper.toResponse(entity);
	}

	@Override
	public String delete(Long id) {
		if (!learnerPathRepository.existsById(id)) {
			throw new ResourceNotFoundException("Learner path not found: " + id);
		}

		learnerPathRepository.deleteById(id);

		return "Learner path deleted successfully";
	}
}