package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;

import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.TopicReference;

public class StudentTopicReferenceProgressResponse {

    private Long id;
    private String studentId;
    private Long referenceId;
    private Boolean completed;
    private LocalDateTime completedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	public Boolean getCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	public LocalDateTime getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}
	
}
