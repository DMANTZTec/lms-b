package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="chapter")
public class Chapter {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String chapter_nm;
private String chapter_desc;
private Long chapter_num;

@ManyToOne
@JoinColumn(name = "course_id")  // FK in CHAPTER table
private Course course;


private Long created_by;
private LocalDateTime created_dt;

private Long updated_by;
private LocalDateTime updated_dt;
public Chapter() {
	super();
	// TODO Auto-generated constructor stub
}
public Chapter(Long id, String chapter_nm, String chapter_desc, Long chapter_num, Course course, Long created_by,
		LocalDateTime created_dt, Long updated_by, LocalDateTime updated_dt) {
	super();
	this.id = id;
	this.chapter_nm = chapter_nm;
	this.chapter_desc = chapter_desc;
	this.chapter_num = chapter_num;
	this.course = course;
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
public String getChapter_nm() {
	return chapter_nm;
}
public void setChapter_nm(String chapter_nm) {
	this.chapter_nm = chapter_nm;
}
public String getChapter_desc() {
	return chapter_desc;
}
public void setChapter_desc(String chapter_desc) {
	this.chapter_desc = chapter_desc;
}
public Long getChapter_num() {
	return chapter_num;
}
public void setChapter_num(Long chapter_num) {
	this.chapter_num = chapter_num;
}
public Course getCourse() {
	return course;
}
public void setCourse(Course course) {
	this.course = course;
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










