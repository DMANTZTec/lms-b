package com.dmantz.lms.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentOtp;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    // Locks the student row so two payments for the same student cannot create duplicate enrollments
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Student s WHERE s.studentId = :studentId")
    Optional<Student> findByStudentIdForUpdate(@Param("studentId") String studentId);


    List<Student> findByStudentIdIn(List<String> studentIds);

    @Query(value = "SELECT MAX(student_id) FROM student", nativeQuery = true)
    String findMaxStudentId();

	boolean existsByStudentId(String studentId);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Student s
        WHERE s.enabled = 'N'
        AND s.createdDt < :cutoffTime
    """)
    int deleteUnverifiedStudents(@Param("cutoffTime") LocalDateTime cutoffTime);

    Optional<Object> findByMobileNum(String mobileNum);

	Optional<Student> findByEmailIdOrMobileNum(String emailIdOrMobileNo, String emailIdOrMobileNo2);
}




