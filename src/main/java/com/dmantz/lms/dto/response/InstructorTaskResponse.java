package com.dmantz.lms.dto.response;

import java.util.List;

public class InstructorTaskResponse {

	private String title;
	private String description;
	private String courseId;
	private Long batchId;
	private int assignedStudentCount;
	private List<StudentTaskResponse> assignedTasks;

	public InstructorTaskResponse() {
	}

	public InstructorTaskResponse(String title, String description, String courseId, Long batchId,
			int assignedStudentCount, List<StudentTaskResponse> assignedTasks) {
		super();
		this.title = title;
		this.description = description;
		this.courseId = courseId;
		this.batchId = batchId;
		this.assignedStudentCount = assignedStudentCount;
		this.assignedTasks = assignedTasks;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public int getAssignedStudentCount() {
		return assignedStudentCount;
	}

	public void setAssignedStudentCount(int assignedStudentCount) {
		this.assignedStudentCount = assignedStudentCount;
	}

	public List<StudentTaskResponse> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(List<StudentTaskResponse> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}
}