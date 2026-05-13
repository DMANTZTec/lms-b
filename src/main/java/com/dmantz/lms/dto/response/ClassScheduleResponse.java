package com.dmantz.lms.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClassScheduleResponse {

    private Long scheduleId;

    private Long classId;
    private String className;

    private Long courseId;
    private String courseName;

    private Long staffId;
    
    private String staffName;

    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String mode;
    private String status;

    private String meetingLink;
    private String location;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	@Override
	public String toString() {
		return "ClassScheduleResponse [scheduleId=" + scheduleId + ", classId=" + classId + ", className=" + className
				+ ", courseId=" + courseId + ", courseName=" + courseName + ", staffId=" + staffId + ", staffName="
				+ staffName + ", classDate=" + classDate + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", mode=" + mode + ", status=" + status + ", meetingLink=" + meetingLink + ", location=" + location
				+ "]";
	}

    
	
}
