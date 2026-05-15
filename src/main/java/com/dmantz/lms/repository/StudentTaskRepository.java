package com.dmantz.lms.repository;

import com.dmantz.lms.entity.StudentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {

    Optional<StudentTask> findByStudent_StudentIdAndTopic_Id(String studentId, Long topicId);
    @Query("""
    	    SELECT COALESCE(SUM(
    	        FUNCTION('TIMESTAMPDIFF', HOUR, st.startDt, st.endDt)
    	    ), 0)
    	    FROM StudentTask st
    	    WHERE st.student.studentId = :studentId
    	    AND st.endDt IS NOT NULL
    	    AND st.startDt IS NOT NULL
    	""")
    	Integer getTotalHoursSpent(@Param("studentId") String studentId);

}
