package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "chapter")
public class Chapter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "chapter_nm")
	private String chapterNm;

	@Column(name = "chapter_desc")
	private String chapterDesc;

	@Column(name = "chapter_num")
	private Long chapterNum;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
	    name = "course_id",
	    referencedColumnName = "course_id",
	    nullable = false
	)
	private Course course;

	@OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Topic> topics = new ArrayList<>();

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_dt")
	private LocalDateTime createdDt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_dt")
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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
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

	@Override
	public String toString() {
		return "Chapter [id=" + id + ", chapterNm=" + chapterNm + ", chapterDesc=" + chapterDesc + ", chapterNum="
				+ chapterNum + ", course=" + course + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + "]";
	}

}