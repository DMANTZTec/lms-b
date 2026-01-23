package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String subject_nm;
private String subject_short_cd;
private String subject_category;
private String description;

private Long created_by;
private LocalDateTime created_dt;

private Long updated_by;
private LocalDateTime updated_dt;
public Subject() {
	super();
	// TODO Auto-generated constructor stub
}

public Subject(Long id, String subject_nm, String subject_short_cd, String subject_category, String description,
		Long created_by, LocalDateTime created_dt, Long updated_by, LocalDateTime updated_dt) {
	super();
	this.id = id;
	this.subject_nm = subject_nm;
	this.subject_short_cd = subject_short_cd;
	this.subject_category = subject_category;
	this.description = description;
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
