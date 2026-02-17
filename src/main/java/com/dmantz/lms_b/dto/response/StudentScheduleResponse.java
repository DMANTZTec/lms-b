package com.dmantz.lms_b.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class StudentScheduleResponse {

    private Long scheduleId;
    private String courseName;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String mode;
    private String meetingLink;
    private String location;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "StudentScheduleResponse{" +
                "scheduleId=" + scheduleId +
                ", courseName='" + courseName + '\'' +
                ", classDate=" + classDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", mode='" + mode + '\'' +
                ", meetingLink='" + meetingLink + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
