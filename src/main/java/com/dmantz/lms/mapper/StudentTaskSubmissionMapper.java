package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.AttachmentRequest;
import com.dmantz.lms.dto.request.StudentTaskSubmissionRequest;
import com.dmantz.lms.dto.response.AttachmentResponse;
import com.dmantz.lms.dto.response.StudentTaskSubmissionResponse;
import com.dmantz.lms.entity.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentTaskSubmissionMapper {

    // ================= REQUEST-> ENTITY =================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentTask", source = "task")
    @Mapping(target = "student", source = "student")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "status", constant = "PENDING_REVIEW")
    @Mapping(target = "submittedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "reviewFeedback", ignore = true)
    @Mapping(target = "pointsAwarded", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    @Mapping(target = "updatedDt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    StudentTaskSubmission toEntity(StudentTaskSubmissionRequest request, StudentTask task, Student student, Staff instructor);
   

    FileAttachment mapAttachment(AttachmentRequest request);

    // ================= ENTITY -> RESPONSE =================
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "studentTask.course.courseTitle", target = "courseTitle")
    @Mapping(source = "studentTask.topic.topicNm", target = "topicName")
    @Mapping(source = "studentTask.title", target = "taskTitle")
    StudentTaskSubmissionResponse toResponse(StudentTaskSubmission submission);


    AttachmentResponse mapAttachmentResponse(FileAttachment dto);
}