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

}
