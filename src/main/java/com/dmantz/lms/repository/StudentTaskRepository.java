package com.dmantz.lms.repository;

import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
    
    List<StudentTask> findByStudent_StudentIdAndStatus(String studentId, StudentTaskStatus completed);

	List<StudentTask> findByStudent_StudentId(String studentId);

	Optional<Student> findByIdAndStudent_StudentId(Long taskid, String studentId);

    

}
