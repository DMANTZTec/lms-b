package com.dmantz.lms.dto.response;

import java.time.LocalDate;

public class BatchSummaryResponse {

    private Long batchId;
    private String courseId;
    private String courseName;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer capacity;
    private long scheduleCount;

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

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

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public long getScheduleCount() {
        return scheduleCount;
    }

    public void setScheduleCount(long scheduleCount) {
        this.scheduleCount = scheduleCount;
    }

    @Override
    public String toString() {
        return "BatchSummaryResponse{" +
                "batchId=" + batchId +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", batchName='" + batchName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", capacity=" + capacity +
                ", scheduleCount=" + scheduleCount +
                '}';
    }
}
