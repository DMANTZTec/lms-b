package com.dmantz.lms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject extends AuditFields {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "subject_nm")
	private String subjectNm;

	@Column(name = "subject_short_cd")
	private String subjectShortCd;

	@Column(name = "subject_category")
	private String subjectCategory;

	@Column(name = "description")
	private String description;

	@Column(name = "subject_image")
	private String subjectImage;

	@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Course> courses = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public String getSubjectImage() {
		return subjectImage;
	}

	public void setSubjectImage(String subjectImage) {
		this.subjectImage = subjectImage;
	}

	@Override
	public String toString() {
		return "Subject [id=" + id + ", subjectNm=" + subjectNm + ", subjectShortCd=" + subjectShortCd
				+ ", subjectCategory=" + subjectCategory + ", description=" + description + ", subjectImage="
				+ subjectImage + ", courses=" + courses + "]";
	}

}
