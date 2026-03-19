package com.dmantz.lms.repository;

import com.dmantz.lms.entity.ClassBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassBatchRepository extends JpaRepository<ClassBatch, Long> {
    @Query("""
           SELECT cb
           FROM ClassBatch cb
           JOIN StudentCourse sc ON sc.course.id = cb.course.id
           WHERE sc.student.id = :studentId
           """)
    List<ClassBatch> findByStudentId(Long studentId);
}
