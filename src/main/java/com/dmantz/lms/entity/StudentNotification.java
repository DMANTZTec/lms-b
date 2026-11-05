package com.dmantz.lms.entity;

import java.time.LocalDateTime;

import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.*;

@Entity
@Table(name = "student_notification")
public class StudentNotification extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false, columnDefinition = "CHAR(7)")
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "staff_id", referencedColumnName = "staff_id", nullable = false)
	private Staff staff;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "batch_id", nullable = false)
	private ClassBatch classBatch;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
	private Course course;

	@Column(name = "message", nullable = false, columnDefinition = "TEXT")
	private String message;

	// Comma separated NotificationChannel names, e.g. "IN_APP,EMAIL"
	@Column(name = "channels", nullable = false, length = 100)
	private String channels;

	@Enumerated(EnumType.STRING)
	@Column(name = "email_status", length = 20)
	private NotificationEmailStatus emailStatus;

	@Column(name = "is_read", nullable = false)
	private boolean read = false;

	@Column(name = "read_at")
	private LocalDateTime readAt;

	@Column(name = "sent_at", nullable = false)
	private LocalDateTime sentAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public ClassBatch getClassBatch() {
		return classBatch;
	}

	public void setClassBatch(ClassBatch classBatch) {
		this.classBatch = classBatch;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public NotificationEmailStatus getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(NotificationEmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public LocalDateTime getReadAt() {
		return readAt;
	}

	public void setReadAt(LocalDateTime readAt) {
		this.readAt = readAt;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}
