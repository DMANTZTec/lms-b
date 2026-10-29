package com.dmantz.lms.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentNeedHelpRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    @NotNull(message = "NeedHelp flag is required")
    private Boolean needHelp;

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

    public Boolean getNeedHelp() {
        return needHelp;
    }

    public void setNeedHelp(Boolean needHelp) {
        this.needHelp = needHelp;
    }
}