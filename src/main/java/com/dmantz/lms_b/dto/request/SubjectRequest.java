package com.dmantz.lms_b.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubjectRequest {

	@NotBlank(message = "Subject name is required")
	@Size(max = 100, message = "Subject name must be at most 100 characters")
	private String subject_nm;

	@NotBlank(message = "Subject short code is required")
	@Size(max = 5, message = "Subject short code must be at most 5 characters")
	private String subject_short_cd;

	@NotBlank(message = "Subject category is required")
	@Size(max = 100, message = "Subject category must be at most 100 characters")
	private String subject_category;

	@Size(max = 500, message = "Description must be at most 500 characters")
	private String description;

	public String getSubject_nm() {
		return subject_nm;
	}

	public void setSubject_nm(String subject_nm) {
		this.subject_nm = subject_nm;
	}

	public String getSubject_category() {
		return subject_category;
	}

	public void setSubject_category(String subject_category) {
		this.subject_category = subject_category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject_short_cd() {
		return subject_short_cd;
	}

	public void setSubject_short_cd(String subject_short_cd) {
		this.subject_short_cd = subject_short_cd;
	}

}
