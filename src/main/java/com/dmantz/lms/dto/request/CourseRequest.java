package com.dmantz.lms.dto.request;

import java.util.List;

import com.dmantz.lms.entity.CourseLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CourseRequest {

	@NotBlank(message = "Course title is required")
	@Size(max = 255, message = "Course title must be at most 255 characters")
	@Pattern(regexp = "^[A-Za-z0-9 \\-&+()]+$", message = "Course title can contain letters, numbers, spaces, hyphens, ampersand, plus, and parentheses only")
	private String courseTitle;

	@NotBlank(message = "description is required")
	private String description;

	@NotBlank(message = "Language is required")
	@Size(max = 50, message = "Language must be at most 50 characters")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Language must contain only letters and spaces")
	private String language;

	@NotEmpty(message = "At least one skill is required")
	private List<String> skills;

	@NotNull(message = "Subject ID is required")
	private Long subjectId;

	@NotNull(message = "Provider ID is required")
	private Long providerId;
	
	@NotNull(message = "Course level is required")
	private CourseLevel level;
	
	@Size(max = 500, message = "Course image URL must be at most 500 characters")
	private String courseImage;
	
	@Size(max = 500, message = "Intro video URL must be at most 500 characters")
	private String introVideo;
	
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
	
	
	

}

