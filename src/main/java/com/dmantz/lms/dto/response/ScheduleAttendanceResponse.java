package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleAttendanceResponse {

    private Long scheduleId;
    private String batchName;
    private String course;
    private String sessionStatus; // NOT_STARTED | IN_PROGRESS | MARKED
    private Integer totalStudents;
    private Integer presentCount;
    private Integer absentCount;
    private Integer unmarkedCount;
    private Double attendanceRate;
    private LocalDateTime markedAt;
    private String markedBy;

    private List<AttendanceStudentResponse> students = new ArrayList<>();

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Integer getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Integer presentCount) {
        this.presentCount = presentCount;
    }

    public Integer getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(Integer absentCount) {
        this.absentCount = absentCount;
    }

    public Integer getUnmarkedCount() {
        return unmarkedCount;
    }

    public void setUnmarkedCount(Integer unmarkedCount) {
        this.unmarkedCount = unmarkedCount;
    }

    public Double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(Double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }

    public String getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(String markedBy) {
        this.markedBy = markedBy;
    }

    public List<AttendanceStudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<AttendanceStudentResponse> students) {
        this.students = students;
    }
}
