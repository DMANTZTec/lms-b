package com.dmantz.lms.repository;

import com.dmantz.lms.entity.ClassTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassTopicRepository extends JpaRepository<ClassTopic, Long> {

    boolean existsByClassBatchIdAndTopicId(Long classBatchId, Long topicId);

    void deleteByClassBatchIdAndTopicIdIn(Long batchId, List<Long> topicIds);

    List<ClassTopic> findByClassBatchId(Long classBatchId);

}
