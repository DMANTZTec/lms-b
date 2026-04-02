package com.dmantz.lms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.dmantz.lms.entity.base.AuditFields;

@Entity
@Table(name = "course")
public class Course extends AuditFields{
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
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Chapter> chapters = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@Enumerated(EnumType.STRING)
	@Column(name = "level")
	private CourseLevel level;

	@Column(name = "course_image")
	private String courseImage;

	@Column(name = "intro_video")
	private String introVideo;


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

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
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

	public CourseLevel getLevel() {
		return level;
	}

	public void setLevel(CourseLevel level) {
		this.level = level;
	}

	public String getCourseImage() {
		return courseImage;
	}

	public void setCourseImage(String courseImage) {
		this.courseImage = courseImage;
	}

	public String getIntroVideo() {
		return introVideo;
	}

	public void setIntroVideo(String introVideo) {
		this.introVideo = introVideo;
	}

	@Override
	public String toString() {
		return "Course{" +
				"id=" + id +
				", courseId='" + courseId + '\'' +
				", courseTitle='" + courseTitle + '\'' +
				", description='" + description + '\'' +
				", language='" + language + '\'' +
				", skills='" + skills + '\'' +
				", chapters=" + chapters +
				", subject=" + subject +
				", provider=" + provider +
				", level=" + level +
				", courseImage='" + courseImage + '\'' +
				", introVideo='" + introVideo + '\'' +
				'}';
	}

}