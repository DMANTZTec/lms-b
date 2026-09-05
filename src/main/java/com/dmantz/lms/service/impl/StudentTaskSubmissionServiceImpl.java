package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentTaskSubmissionRequest;
import com.dmantz.lms.dto.response.StudentTaskSubmissionResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTaskSubmissionMapper;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.StudentTaskSubmissionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentTaskSubmissionServiceImpl implements StudentTaskSubmissionService {

    private static final Logger logger = LogManager.getLogger(StudentTaskSubmissionServiceImpl.class);

    private final StudentTaskSubmissionRepository submissionRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final StudentRepository studentRepository;
    private final StaffCourseRepository staffCourseRepository;
    private final StudentTaskSubmissionMapper submissionMapper;

    public StudentTaskSubmissionServiceImpl(StudentTaskSubmissionRepository submissionRepository,
            StudentTaskRepository studentTaskRepository,
            StudentRepository studentRepository,
            StaffCourseRepository staffCourseRepository,
            StudentTaskSubmissionMapper submissionMapper) {

        this.submissionRepository = submissionRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.studentRepository = studentRepository;
        this.staffCourseRepository = staffCourseRepository;
        this.submissionMapper = submissionMapper;
    }

    @Override
    public StudentTaskSubmissionResponse submitTask(StudentTaskSubmissionRequest request) {

        logger.info("Submitting task for studentId: {} taskId: {}",
                request.getStudentId(), request.getStudentTaskId());

        Student student = studentRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        StudentTask task = studentTaskRepository.findById(request.getStudentTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + request.getStudentTaskId()));

        if (!task.getStudent().getStudentId().equals(request.getStudentId())) {
            throw new IllegalStateException("This task does not belong to the given student");
        }

        List<StaffCourse> staffCourses = staffCourseRepository
                .findByCourse_CourseId(task.getCourse().getCourseId());

        if (staffCourses.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No instructors assigned to course: " + task.getCourse().getCourseId());
        }

        if (staffCourses.size() > 1) {
            logger.warn("Course {} has {} instructors assigned; defaulting to the first for this submission's reviewer",
                    task.getCourse().getCourseId(), staffCourses.size());
        }

        
        Staff instructor = staffCourses.get(0).getStaff();

        StudentTaskSubmission submission = submissionMapper.toEntity(request, task, student, instructor);

        StudentTaskSubmission saved = submissionRepository.save(submission);

       
        task.setStatus(StudentTaskStatus.SUBMITTED);
        studentTaskRepository.save(task);

        logger.info("Task submission {} is pending review from instructor: {}",
                saved.getId(), instructor.getStaffId());

        logger.info("Task submitted successfully with submissionId: {}", saved.getId());

        return submissionMapper.toResponse(saved);
    }
}