package com.dmantz.lms.repository;

import com.dmantz.lms.entity.LearnerPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearnerPathRepository extends JpaRepository<LearnerPath, Long> {

    List<LearnerPath> findByIsActiveTrueOrderByDisplayOrderAsc();
}