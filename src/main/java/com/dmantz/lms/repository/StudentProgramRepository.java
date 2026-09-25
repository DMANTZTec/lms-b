package com.dmantz.lms.repository;

import com.dmantz.lms.entity.StudentProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProgramRepository extends JpaRepository<StudentProgram,Long> {

    boolean existsByStudent_StudentIdAndProgram_ProgramId(String studentId, String programId);
}
