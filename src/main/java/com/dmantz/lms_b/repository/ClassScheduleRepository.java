package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.ClassSchedule;
import com.dmantz.lms_b.entity.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

    @Query("""
    SELECT cs
    FROM ClassSchedule cs
    JOIN cs.classBatch cb
    JOIN cb.course c
    JOIN StudentCourse sc ON sc.course.id = c.id
    JOIN sc.student s
    WHERE s.studentId = :studentId
      AND cs.classDate BETWEEN :start AND :end
      AND cs.status = :status
""")
    List<ClassSchedule> findWeeklySchedule(
            @Param("studentId") String studentId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("status") ClassStatus status);
}
