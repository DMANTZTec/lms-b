package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="course")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String course_id;
	private String course_title;
	private String description;
	private String language;
	private String skills;

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;

	private Long created_by;
	private LocalDateTime created_dt;

	private Long updated_by;
	private LocalDateTime updated_dt;

	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Course(Long id, String course_id, String course_title, String description, String language, String skills,
			Subject subject, Provider provider, Long created_by, LocalDateTime created_dt, Long updated_by,
			LocalDateTime updated_dt) {
		super();
		this.id = id;
		this.course_id = course_id;
		this.course_title = course_title;
		this.description = description;
		this.language = language;
		this.skills = skills;
		this.subject = subject;
		this.provider = provider;
		this.created_by = created_by;
		this.created_dt = created_dt;
		this.updated_by = updated_by;
		this.updated_dt = updated_dt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getCourse_title() {
		return course_title;
	}

	public void setCourse_title(String course_title) {
		this.course_title = course_title;
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

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public LocalDateTime getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(LocalDateTime created_dt) {
		this.created_dt = created_dt;
	}

	public Long getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Long updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDateTime getUpdated_dt() {
		return updated_dt;
	}

	public void setUpdated_dt(LocalDateTime updated_dt) {
		this.updated_dt = updated_dt;
	}

}
