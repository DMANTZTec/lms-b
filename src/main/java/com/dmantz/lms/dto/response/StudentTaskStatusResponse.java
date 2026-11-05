package com.dmantz.lms.dto.response;

public class StudentTaskStatusResponse {

    private Long taskId;
    private Long studentTaskId;
    private Long submissionId;
    private String status; // NOT_SUBMITTED | PENDING_REVIEW | REVIEWED

    public StudentTaskStatusResponse() {
    }

    public StudentTaskStatusResponse(Long taskId, Long studentTaskId, Long submissionId, String status) {
        this.taskId = taskId;
        this.studentTaskId = studentTaskId;
        this.submissionId = submissionId;
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getStudentTaskId() {
        return studentTaskId;
    }

    public void setStudentTaskId(Long studentTaskId) {
        this.studentTaskId = studentTaskId;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
