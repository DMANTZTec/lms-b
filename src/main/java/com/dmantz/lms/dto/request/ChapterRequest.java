package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ChapterRequest {

	@NotBlank
	private String courseId;

	@NotBlank(message = "Chapter name is required")
	private String chapterNm;

	@NotBlank(message = "Chapter description is required")
	private String chapterDesc;

	public String getChapterNm() {
		return chapterNm;
	}

	public void setChapterNm(String chapterNm) {
		this.chapterNm = chapterNm;
	}

	public String getChapterDesc() {
		return chapterDesc;
	}

	public void setChapterDesc(String chapterDesc) {
		this.chapterDesc = chapterDesc;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

}
