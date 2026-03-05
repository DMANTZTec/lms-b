package com.dmantz.lms_b.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms_b.entity.StudentTopicReferenceProgress;

public interface StudentTopicReferenceProgressRepository  extends JpaRepository<StudentTopicReferenceProgress, Long>{
	List<StudentTopicReferenceProgress> findByStudent_IdAndTopicReference_Topic_Id(
            Long studentId,
            Long topicId
    );

    Optional<StudentTopicReferenceProgress> findByStudent_IdAndTopicReference_Id(
            Long studentId,
            Long referenceId
    );
    
  
}
