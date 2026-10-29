package com.dmantz.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.SuccessStory;

public interface SuccessStoryRepository extends JpaRepository<SuccessStory, Long> {
	    List<SuccessStory> findByIsActiveTrueOrderByDisplayOrderAsc();
	}
