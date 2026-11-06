package com.dmantz.lms.dto.request;

import java.util.List;

import com.dmantz.lms.entity.NotificationChannel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// One batch/course and one channel selection, with a personalised message per student.
public class SendBulkStudentNotificationRequest {

	@NotNull(message = "Batch ID is required")
	private Long batchId;

	@NotBlank(message = "Course ID is required")
	private String courseId;

	// Optional: when missing or empty, every student is notified via both in-app and email.
	private List<@NotNull NotificationChannel> channels;

	@NotEmpty(message = "At least one student is required")
	@Size(max = 500, message = "At most 500 students can be notified in one request")
	private List<@Valid @NotNull StudentMessage> students;

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

	public List<NotificationChannel> getChannels() {
		return channels;
	}

	public void setChannels(List<NotificationChannel> channels) {
		this.channels = channels;
	}

	public List<StudentMessage> getStudents() {
		return students;
	}

	public void setStudents(List<StudentMessage> students) {
		this.students = students;
	}

	public static class StudentMessage {

		@NotBlank(message = "Student ID is required")
		private String studentId;

		@NotBlank(message = "Message is required")
		@Size(max = 2000, message = "Message must be at most 2000 characters")
		private String message;

		public String getStudentId() {
			return studentId;
		}

		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
