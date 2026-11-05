package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.dmantz.lms.entity.NotificationChannel;
import com.dmantz.lms.entity.NotificationEmailStatus;

public class StudentNotificationResponse {

	private Long notificationId;
	private String studentId;
	private String instructorId;
	private String instructorName;
	private Long batchId;
	private String batchName;
	private String courseId;
	private String courseTitle;
	private String message;
	private List<NotificationChannel> channels;
	private NotificationEmailStatus emailStatus;
	private LocalDateTime sentAt;

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(String instructorId) {
		this.instructorId = instructorId;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
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

	public NotificationEmailStatus getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(NotificationEmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}
