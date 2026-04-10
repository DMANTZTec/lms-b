package com.dmantz.lms.service.impl;


import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentNeedHelpRequest;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.StudentTaskStatus;
import com.dmantz.lms.entity.Topic;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentTaskMapper;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.repository.StudentTaskRepository;
import com.dmantz.lms.repository.TopicRepository;
import com.dmantz.lms.service.StudentTaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentTaskServiceImpl implements StudentTaskService {

    private final StudentTaskRepository studentTaskRepository;
    private final StudentRepository studentRepository;
    private final TopicRepository topicRepository;
    private final StudentTaskMapper studentTaskMapper;

    public StudentTaskServiceImpl(
            StudentTaskRepository studentTaskRepository,
            StudentRepository studentRepository,
            TopicRepository topicRepository,
            StudentTaskMapper studentTaskMapper) {

        this.studentTaskRepository = studentTaskRepository;
        this.studentRepository = studentRepository;
        this.topicRepository = topicRepository;
        this.studentTaskMapper = studentTaskMapper;
    }

    @Override
    public StudentTaskResponse addTask(StudentTaskRequest request) {

        Student student = studentRepository
                .findByStudentId(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        Topic topic = topicRepository
                .findById(request.getTopicId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found: " + request.getTopicId()));

        StudentTask task = studentTaskRepository
                .findByStudent_StudentIdAndTopic_Id(request.getStudentId(), request.getTopicId())
                .orElseGet(() -> {

                    StudentTask newTask = new StudentTask();
                    newTask.setStudent(student);
                    newTask.setTopic(topic);
                    newTask.setStartDt(LocalDateTime.now());
                    newTask.setStatus(StudentTaskStatus.NOT_STARTED);
                    newTask.setNeedHelp(false);

                    return studentTaskRepository.save(newTask);
                });

        return studentTaskMapper.toResponse(task);
    }

    @Override
    public StudentTaskResponse updateNeedHelp(StudentNeedHelpRequest request) {

        StudentTask task = studentTaskRepository
                .findByStudent_StudentIdAndTopic_Id(
                        request.getStudentId(),
                        request.getTopicId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found for student: "
                                        + request.getStudentId()
                                        + " and topic: "
                                        + request.getTopicId()));

        task.setNeedHelp(request.getNeedHelp());

        StudentTask updatedTask = studentTaskRepository.save(task);

        return studentTaskMapper.toResponse(updatedTask);
    }


}