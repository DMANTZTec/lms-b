package com.dmantz.lms.repository;

import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.ClassStatus;
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
    
    
    
    @Query("""
    	       select cs
    	       from ClassSchedule cs
    	       join cs.classBatch cb
    	       join cb.course c
    	       join StudentCourse sc on sc.course.id = c.id
    	       join sc.student s
    	       where s.studentId = :studentId
    	       """)
    	List<ClassSchedule> findAllSchedulesForStudent(@Param("studentId") String studentId);


    List<ClassSchedule> findByStaff_StaffId(String staffId);

    List<ClassSchedule> findByStaffStaffIdAndClassDate(String staffId, LocalDate classDate);
    
 // ClassScheduleRepository
 	List<ClassSchedule> findByClassBatch_Id(Long batchId);
 	
 	  List<ClassSchedule>
 	    findByClassBatchIdInAndClassDateBetweenOrderByClassDateAscStartTimeAsc(
 	            List<Long> batchIds,
 	            LocalDate startDate,
 	            LocalDate endDate
 	    );
 	  
 	    List<ClassSchedule> findByStaff_StaffIdAndClassDateBetween(
 	            String staffId,
 	            LocalDate startDate,
 	            LocalDate endDate
 	    );

    // Matches a schedule for a given instructor via: the legacy single staff
    // column (old data), the schedule's own instructors join table (current
    // design), or — when a schedule has no instructors of its own yet — the
    // instructor being on the schedule's batch (legacy-data fallback).
    @Query("""
        SELECT DISTINCT cs
        FROM ClassSchedule cs
        LEFT JOIN cs.staff st
        LEFT JOIN cs.instructors si
        LEFT JOIN cs.classBatch cb
        LEFT JOIN cb.instructors bi
        WHERE st.staffId = :staffId
           OR si.staffId = :staffId
           OR (si IS NULL AND bi.staffId = :staffId)
        """)
    List<ClassSchedule> findAllForInstructor(@Param("staffId") String staffId);

    @Query("""
        SELECT DISTINCT cs
        FROM ClassSchedule cs
        LEFT JOIN cs.staff st
        LEFT JOIN cs.instructors si
        LEFT JOIN cs.classBatch cb
        LEFT JOIN cb.instructors bi
        WHERE (st.staffId = :staffId
           OR si.staffId = :staffId
           OR (si IS NULL AND bi.staffId = :staffId))
          AND cs.classDate = :classDate
        """)
    List<ClassSchedule> findForInstructorAndClassDate(
            @Param("staffId") String staffId,
            @Param("classDate") LocalDate classDate);

    @Query("""
        SELECT DISTINCT cs
        FROM ClassSchedule cs
        LEFT JOIN cs.staff st
        LEFT JOIN cs.instructors si
        LEFT JOIN cs.classBatch cb
        LEFT JOIN cb.instructors bi
        WHERE (st.staffId = :staffId
           OR si.staffId = :staffId
           OR (si IS NULL AND bi.staffId = :staffId))
          AND cs.classDate BETWEEN :startDate AND :endDate
        """)
    List<ClassSchedule> findForInstructorAndClassDateBetween(
            @Param("staffId") String staffId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
