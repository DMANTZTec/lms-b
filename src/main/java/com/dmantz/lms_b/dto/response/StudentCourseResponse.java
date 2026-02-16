package com.dmantz.lms_b.dto.response;

public class StudentCourseResponse {

    private Long id;
    private String studentId;
    private String courseId;
    private String status;
    private Double progressPercentage;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    @Override
    public String toString() {
        return "StudentCourseResponse{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", status='" + status + '\'' +
                ", progressPercentage=" + progressPercentage +
                '}';
    }
}
