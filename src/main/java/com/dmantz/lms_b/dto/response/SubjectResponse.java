package com.dmantz.lms_b.dto.response;

import java.time.LocalDateTime;

public class SubjectResponse {

	private String subject_nm;
	private String subject_short_cd;
	private String subject_category;
	private String description;

	private Long created_by;
	private LocalDateTime created_dt;
	private Long updated_by;
	private LocalDateTime updated_dt;

	public String getSubject_nm() {
		return subject_nm;
	}

	public void setSubject_nm(String subject_nm) {
		this.subject_nm = subject_nm;
	}

	public String getSubject_short_cd() {
		return subject_short_cd;
	}

	public void setSubject_short_cd(String subject_short_cd) {
		this.subject_short_cd = subject_short_cd;
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
