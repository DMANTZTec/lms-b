package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.StudentTaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentTaskUpdateRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    private String commitUrl;

    private StudentTaskStatus status;

    private Boolean needHelp;

    private String studentCommentTxt;

    private String reviewerCommentTxt;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getCommitUrl() {
        return commitUrl;
    }

    public void setCommitUrl(String commitUrl) {
        this.commitUrl = commitUrl;
    }

    public StudentTaskStatus getStatus() {
        return status;
    }

    public void setStatus(StudentTaskStatus status) {
        this.status = status;
    }

    public Boolean getNeedHelp() {
        return needHelp;
    }

    public void setNeedHelp(Boolean needHelp) {
        this.needHelp = needHelp;
    }

    public String getStudentCommentTxt() {
        return studentCommentTxt;
    }

    public void setStudentCommentTxt(String studentCommentTxt) {
        this.studentCommentTxt = studentCommentTxt;
    }

    public String getReviewerCommentTxt() {
        return reviewerCommentTxt;
    }

    public void setReviewerCommentTxt(String reviewerCommentTxt) {
        this.reviewerCommentTxt = reviewerCommentTxt;
    }
}