
package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.dmantz.lms.entity.CourseLevel;

public class CourseResponse {
	private Long id;
	private String courseId;
	private String courseTitle;

	private String description;

	private String language;

	private List<String> skills;

	private Long subjectId;
	
	private String subjectNm;

	private Long providerId;

	private CourseLevel level;

	private String courseImage;

	private String introVideo;

	private Long createdBy;
	private LocalDateTime createdDt;
	private Long updatedBy;
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

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
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

	public String getSubjectNm() {
		return subjectNm;
	}

	public void setSubjectNm(String subjectNm) {
		this.subjectNm = subjectNm;
	}


	
}
