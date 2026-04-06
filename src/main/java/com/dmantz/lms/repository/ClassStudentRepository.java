package com.dmantz.lms.repository;

import com.dmantz.lms.entity.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long> {

    boolean existsByClassBatchIdAndStudentId(Long classBatchId, Long studentId);

}
