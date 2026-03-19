package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.ClassMode;
import com.dmantz.lms.entity.ClassStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClassResponse {

    private Long batchId;
    private Long courseId;
    private String courseName;

    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer capacity;

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "ClassResponse{" +
                "batchId=" + batchId +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", className='" + className + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", capacity=" + capacity +
                '}';
    }
}
