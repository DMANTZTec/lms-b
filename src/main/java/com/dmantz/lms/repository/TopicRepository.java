package com.dmantz.lms.repository;

import com.dmantz.lms.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    // Get all topics under a chapter ordered by topicNum
    List<Topic> findByChapterIdOrderByTopicNumAsc(Long chapterId);

    // Get max topic number inside a chapter
    @Query("SELECT MAX(t.topicNum) FROM Topic t WHERE t.chapter.id = :chapterId")
    Long findMaxTopicNumByChapterId(@Param("chapterId") Long chapterId);

    // Find specific topic inside chapter
    Optional<Topic> findByIdAndChapterId(Long topicId, Long chapterId);
    
    
    boolean existsByChapter_IdAndTopicNm(Long chapterId, String topicNm);

}
