package com.dmantz.lms.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class StudentTaskSubmissionRequest {

    @NotNull(message = "Student task ID is required")
    private Long studentTaskId;

    @NotNull(message = "Student ID is required")
    private String studentId;

    private String submissionNotes;

    @NotEmpty(message = "At least one attachment is required")
    @Valid
    private List<AttachmentRequest> attachments;

    public Long getStudentTaskId() {
        return studentTaskId;
    }

    public void setStudentTaskId(Long studentTaskId) {
        this.studentTaskId = studentTaskId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubmissionNotes() {
        return submissionNotes;
    }

    public void setSubmissionNotes(String submissionNotes) {
        this.submissionNotes = submissionNotes;
    }

    public List<AttachmentRequest> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentRequest> attachments) {
        this.attachments = attachments;
    }
}