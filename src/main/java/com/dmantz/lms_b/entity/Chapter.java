package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dmantz.lms_b.entity.base.AuditFields;

import jakarta.persistence.*;

@Entity
@Table(name = "chapter")
public class Chapter extends AuditFields{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "chapter_nm")
	private String chapterNm;

	@Column(name = "chapter_desc")
	private String chapterDesc;

	@Column(name = "chapter_num")
	private int chapterNum;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
	    name = "course_id",
	    referencedColumnName = "course_id",
	    nullable = false
	)
	private Course course;

	@OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Topic> topics = new ArrayList<>();

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

	public int getChapterNum() {
		return chapterNum;
	}

	public void setChapterNum(int chapterNum) {
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

	@Override
	public String toString() {
		return "Chapter [id=" + id + ", chapterNm=" + chapterNm + ", chapterDesc=" + chapterDesc + ", chapterNum="
				+ chapterNum + ", course=" + course + ", topics=" + topics + "]";
	}

	
}