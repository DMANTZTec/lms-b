package com.dmantz.lms.repository;

import com.dmantz.lms.entity.TopicReference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicReferenceRepository extends JpaRepository<TopicReference, Long> {
	
	List<TopicReference> findByTopicId(Long topicId);
}
