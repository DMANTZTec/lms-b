package com.dmantz.lms.repository;

import com.dmantz.lms.entity.StudentTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {

    Optional<StudentTask> findByStudent_StudentIdAndTopic_Id(String studentId, Long topicId);

}
