package com.dmantz.lms_b.dto.response;

import java.time.LocalDateTime;

public class SubjectResponse {

	private String subjectNm;
	private String subjectShortCd;
	private String subjectCategory;
	private String description;

	private Long createdBy;
	private LocalDateTime createdDt;
	private Long updatedBy;
	private LocalDateTime updatedDt;
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
