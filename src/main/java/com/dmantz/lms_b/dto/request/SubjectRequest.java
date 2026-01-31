package com.dmantz.lms_b.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SubjectRequest {

	@NotBlank(message = "Subject name is required")
	@Size(max = 100, message = "Subject name must be at most 100 characters")
	@Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$", message = "Subject name must contain only letters and single spaces")
	private String subjectNm;

	@NotBlank(message = "Subject short code is required")
	@Size(max = 2, message = "Subject short code must be at most 2 characters")
	@Pattern(regexp = "^[A-Z]+$", message = "Subject short code must contain only uppercase letters")
	private String subjectShortCd;

	@NotBlank(message = "Subject category is required")
	@Size(max = 100, message = "Subject category must be at most 100 characters")
	@Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$", message = "Subject category must contain only letters and single spaces")
	private String subjectCategory;

	@Size(max = 500, message = "Description must be at most 500 characters")

	private String description;

	public String getSubjectNm() {
		return subjectNm;
	}

	public void setSubjectNm(String subjectNm) {
		this.subjectNm = subjectNm;
	}

	public String getSubjectShortCd() {
		return subjectShortCd;
	}

	public void setSubjectShortCd(String subjectShortCd) {
		this.subjectShortCd = subjectShortCd;
	}

	public String getSubjectCategory() {
		return subjectCategory;
	}

	public void setSubjectCategory(String subjectCategory) {
		this.subjectCategory = subjectCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
