package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmailId(String email);

    boolean existsByMobileNum(String mobile);

    Student findByEmailIdOrMobileNumOrLoginId(String emailId, String mobileNum, String loginId);

    Optional<Student> findByEmailId(String email);

//    List<Student> findByStudentIdOrderByCreatedDtDesc(String studentId);

    Optional<Student> findByStudentId(String studentId);


}




