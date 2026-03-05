package com.dmantz.lms_b.dto.response;

import java.time.LocalDate;

public class MyCourseResponse {

    private String courseId;
    private String courseName;
    private String status;
    private double progress;
    private LocalDate startDate;
    private LocalDate endDate;


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
