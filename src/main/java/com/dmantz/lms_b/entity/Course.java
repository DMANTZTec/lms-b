package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "course_id")
	private String courseId;

	@Column(name = "course_title")
	private String courseTitle;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "language")
	private String language;

	@Column(name = "skills", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	private String skills;

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_dt")
	private LocalDateTime createdDt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_dt")
	private LocalDateTime updatedDt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(LocalDateTime createdDt) {
		this.createdDt = createdDt;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(LocalDateTime updatedDt) {
		this.updatedDt = updatedDt;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseId=" + courseId + ", courseTitle=" + courseTitle + ", description="
				+ description + ", language=" + language + ", skills=" + skills + ", subject=" + subject + ", provider="
				+ provider + ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", updatedBy=" + updatedBy
				+ ", updatedDt=" + updatedDt + "]";
	}

}