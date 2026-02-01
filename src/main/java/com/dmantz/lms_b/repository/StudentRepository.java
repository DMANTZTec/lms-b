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

    @Query("select count(s) > 0 from Student s where s.email_id = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("select count(s) > 0 from Student s where s.mobile_num = :mobile")
    boolean existsByMobile(@Param("mobile") String mobile);

    @Query("select s from Student s where s.email_id = :username or s.mobile_num = :username or s.login_id = :username")
    Student findByUsername(@Param("username") String username);


//    @Query("SELECT s FROM Student s WHERE s.student_id = :student_id")
//    Optional<Student> findByStudentId(@Param("student_id") String student_id);

    @Query("SELECT s FROM Student s WHERE s.email_id = :email")
    Optional<Student> findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Student s WHERE s.student_id = :studentId ORDER BY s.created_dt DESC")
    List<Student> findByStudentIdOrdered(@Param("studentId") String studentId);


    @Query("SELECT s FROM Student s WHERE s.student_id = :studentId")
    Optional<Student> findByStudentId(@Param("studentId") String studentId);
}




