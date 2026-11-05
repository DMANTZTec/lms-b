package com.dmantz.lms.dto.request;

import java.util.List;

import com.dmantz.lms.entity.NotificationChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SendStudentNotificationRequest {

	@NotBlank(message = "Student ID is required")
	private String studentId;

	@NotNull(message = "Batch ID is required")
	private Long batchId;

	@NotBlank(message = "Course ID is required")
	private String courseId;

	@NotBlank(message = "Message is required")
	@Size(max = 2000, message = "Message must be at most 2000 characters")
	private String message;

	// Optional: when missing or empty, the notification is sent via both in-app and email.
	private List<@NotNull NotificationChannel> channels;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<NotificationChannel> getChannels() {
		return channels;
	}

	public void setChannels(List<NotificationChannel> channels) {
		this.channels = channels;
	}
}
