package com.dmantz.lms.dto.response;

import java.time.LocalDate;

public class InstructorBatchResponse {

	private Long batchId;
	private String className;
	private String courseId;
	private String courseName;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;

	public InstructorBatchResponse() {
	}

	public InstructorBatchResponse(Long batchId, String className, String courseId, String courseName,
			LocalDate startDate, LocalDate endDate, String status) {
		this.batchId = batchId;
		this.className = className;
		this.courseId = courseId;
		this.courseName = courseName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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
}
