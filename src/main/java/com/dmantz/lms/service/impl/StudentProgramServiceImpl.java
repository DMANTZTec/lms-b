package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.AssignProgramRequest;
import com.dmantz.lms.dto.response.AssignProgramResponse;
import com.dmantz.lms.entity.CourseStatus;
import com.dmantz.lms.entity.ProgramEnrollmentStatus;
import com.dmantz.lms.entity.StudentCourse;
import com.dmantz.lms.entity.StudentProgram;
import com.dmantz.lms.exceptions.DuplicateEnrollmentException;
import com.dmantz.lms.exceptions.ProgramNotFoundException;
import com.dmantz.lms.exceptions.StudentNotFoundException;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.StudentProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentProgramServiceImpl implements StudentProgramService {
    private final StudentProgramRepository studentProgramRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final ProgramCourseRepository programCourseRepository;
    private final StudentCourseRepository studentCourseRepository;


    public StudentProgramServiceImpl(StudentProgramRepository studentProgramRepository, StudentRepository studentRepository, ProgramRepository programRepository, ProgramCourseRepository programCourseRepository, StudentCourseRepository studentCourseRepository) {
        this.studentProgramRepository = studentProgramRepository;
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
        this.programCourseRepository = programCourseRepository;
        this.studentCourseRepository = studentCourseRepository;
    }

    @Override
    @Transactional
    public AssignProgramResponse assignProgramToStudent(AssignProgramRequest request) {
        if (studentProgramRepository.existsByStudent_StudentIdAndProgram_ProgramId(
                request.getStudentId(), request.getProgramId())) {
            throw new DuplicateEnrollmentException(
                    "Student " + request.getStudentId() +
                            " is already enrolled in program " + request.getProgramId());
        }
        var program = programRepository
                .findByProgramId(request.getProgramId())
                .orElseThrow(() -> new ProgramNotFoundException("Program not found: " + request.getProgramId()));

        var student = studentRepository
                .findByStudentId(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found" + request.getStudentId()));


        StudentProgram studentProgram = new StudentProgram();
        studentProgram.setProgram(program);
        studentProgram.setStudent(student);
        studentProgram.setStatus(ProgramEnrollmentStatus.PLANNED);
        studentProgram.setEnrollmentDate(LocalDate.now());
        studentProgramRepository.save(studentProgram);

        List<String> enrolledCourseIds = new ArrayList<>();
        var programCourses = programCourseRepository.findCoursesByProgramId(request.getProgramId());

        for (var course : programCourses) {
            boolean alreadyEnrolled = studentCourseRepository.existsByStudent_StudentIdAndCourse_CourseId(student.getStudentId(), course.getCourseId());

            if(!alreadyEnrolled) {
                var studentCourse = new StudentCourse();
                studentCourse.setCourse(course);
                studentCourse.setStudent(student);
                studentCourse.setStatus(CourseStatus.PLANNED);
                studentCourseRepository.save(studentCourse);
                enrolledCourseIds.add(course.getCourseId());
            }
        }

        AssignProgramResponse response = new AssignProgramResponse();
        response.setEnrollmentId(studentProgram.getId());
        response.setProgramId(program.getProgramId());
        response.setStudentId(student.getStudentId());
        response.setProgramTitle(program.getProgramTitle());
        response.setStatus(studentProgram.getStatus());
        response.setEnrolledCourseIds(enrolledCourseIds);

        return response;
    }

}
