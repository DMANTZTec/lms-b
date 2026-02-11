package com.dmantz.lms_b.dto.response;

import java.time.LocalDateTime;

public class ChapterResponse {

	private Long id;
	private String chapterNm;
	private String chapterDesc;
	private Long chapterNum;
	private String courseId;
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

	public Long getChapterNum() {
		return chapterNum;
	}

	public void setChapterNum(Long chapterNum) {
		this.chapterNum = chapterNum;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

}
