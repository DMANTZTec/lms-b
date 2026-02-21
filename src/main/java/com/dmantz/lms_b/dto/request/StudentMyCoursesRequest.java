package com.dmantz.lms_b.dto.request;

import com.dmantz.lms_b.entity.CourseStatus;

public class StudentMyCoursesRequest {
    private String studentId;
    private CourseStatus status; // optional filter

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudentMyCoursesRequest{" +
                "studentId='" + studentId + '\'' +
                ", status=" + status +
                '}';
    }
}
