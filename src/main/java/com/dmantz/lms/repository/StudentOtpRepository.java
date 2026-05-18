package com.dmantz.lms.repository;

import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentOtpRepository extends JpaRepository<StudentOtp, UUID> {


    List<StudentOtp> findByStudent_StudentIdOrderByCreatedDtDesc(String studentId);

    Optional<StudentOtp> findTopByStudentOrderByCreatedDtDesc(Student student);

    @Query("SELECT o FROM StudentOtp o WHERE o.student.studentId = :studentId ORDER BY o.createdDt DESC")
    List<StudentOtp> findLatestOtpByStudentId(@Param("studentId") String studentId);
}
