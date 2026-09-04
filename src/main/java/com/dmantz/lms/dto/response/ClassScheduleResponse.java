package com.dmantz.lms.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClassScheduleResponse {
    private Long scheduleId;
	private Long batchId;
	private String batchName;
	private String className;
	private LocalDate classDate;
	private String dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	// Instructors assigned to this schedule (inherited from its batch, plus any
	// added directly via assign-instructor).
	private List<BatchInstructorResponse> instructors;
	private String mode;
	private String meetingLink;
	private String location;
	private String status;


	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public LocalDate getClassDate() {
		return classDate;
	}

	public void setClassDate(LocalDate classDate) {
		this.classDate = classDate;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public List<BatchInstructorResponse> getInstructors() {
		return instructors;
	}

	public void setInstructors(List<BatchInstructorResponse> instructors) {
		this.instructors = instructors;
	}

}