package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.ClassBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassBatchRepository extends JpaRepository<ClassBatch, Long> {

}
