package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AssignCourseRequest {

    @NotBlank
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
