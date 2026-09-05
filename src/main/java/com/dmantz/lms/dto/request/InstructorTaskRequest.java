package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InstructorTaskRequest {

	@NotBlank(message = "Title is required")
	private String title;

	private String description;

	@NotBlank(message = "Course ID is required")
	private String courseId;

	@NotNull(message = "Batch ID is required")
	private Long batchId;

	@NotNull(message = "Chapter ID is required")
	private Long chapterId;

	@NotNull(message = "Topic ID is required")
	private Long topicId;
	@NotBlank(message = "Assigned by is required")
	private String assignedBy;

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

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}
}