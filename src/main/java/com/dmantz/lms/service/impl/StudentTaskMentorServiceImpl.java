package com.dmantz.lms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmantz.lms.dto.request.AcknowledgeMentorRequest;
import com.dmantz.lms.dto.request.StudentTaskMentorRequest;
import com.dmantz.lms.dto.request.UpdateMentorMinutesRequest;
import com.dmantz.lms.dto.response.StudentTaskMentorResponse;
import com.dmantz.lms.entity.MentorHelpStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskMentor;
import com.dmantz.lms.mapper.StudentTaskMentorMapper;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.repository.StudentTaskMentorRepository;
import com.dmantz.lms.repository.StudentTaskRepository;
import com.dmantz.lms.service.StudentTaskMentorService;

@Service
public class StudentTaskMentorServiceImpl implements StudentTaskMentorService {

    @Autowired
    private StudentTaskMentorRepository mentorRepo;

    @Autowired
    private StudentTaskRepository taskRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudentTaskMentorMapper mapper;

    @Override
    public StudentTaskMentorResponse createMentoringActivity(
            StudentTaskMentorRequest request) {

        // ✅ Validate Task
        StudentTask task = taskRepo.findById(request.getStudentTaskId())
                .orElseThrow(() -> new RuntimeException("StudentTask not found"));

        // ✅ Validate Mentor
        Student mentor = studentRepo.findByStudentId(request.getMentorStudentId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        // ✅ Map
        StudentTaskMentor entity = mapper.toEntity(request);

        // ✅ Set relationships
        entity.setStudentTask(task);
        entity.setMentorStudent(mentor);

        // ✅ Default values
        entity.setStatus(MentorHelpStatus.IN_PROGRESS);
        

        // ✅ Save
        StudentTaskMentor saved = mentorRepo.save(entity);

        return mapper.toDto(saved);
    }
    @Override
    public StudentTaskMentorResponse updateMentoringMinutes(
            Long id, UpdateMentorMinutesRequest request) {

        StudentTaskMentor entity = mentorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentoring record not found"));

        if (request.getMinsSpent() <= 0) {
            throw new RuntimeException("Minutes must be greater than 0");
        }

        entity.setMinsSpent(request.getMinsSpent());
        entity.setStatus(MentorHelpStatus.IN_PROGRESS);

        StudentTaskMentor updated = mentorRepo.save(entity);

        return mapper.toDto(updated);
    }

    @Override
    public StudentTaskMentorResponse acknowledgeMentorHelp(
            Long id) {


        StudentTaskMentor entity = mentorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentoring record not found"));

        if (Boolean.TRUE.equals(entity.getStudentAck())) {
            throw new RuntimeException("Already acknowledged");
        }

        entity.setStudentAck(true);
        entity.setStatus(MentorHelpStatus.COMPLETED);

        StudentTaskMentor updated = mentorRepo.save(entity);

        return mapper.toDto(updated);
    }

}
