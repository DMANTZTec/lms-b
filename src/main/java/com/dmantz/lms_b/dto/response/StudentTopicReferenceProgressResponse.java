package com.dmantz.lms_b.dto.response;

import java.time.LocalDateTime;

import com.dmantz.lms_b.entity.Student;
import com.dmantz.lms_b.entity.TopicReference;

public class StudentTopicReferenceProgressResponse {

    private Long id;
    private Long studentId;
    private Long referenceId;
    private Boolean completed;
    private LocalDateTime completedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
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
