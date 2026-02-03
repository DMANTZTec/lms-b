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
@Table(name = "topic")
public class Topic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String topic_nm;
	private Long topic_num;
	private String description;
	private Long expected_time_min;

	@ManyToOne
	@JoinColumn(name = "chapter_id") // FK in TOPIC table
	private Chapter chapter;

	private Long created_by;
	private LocalDateTime created_dt;

	private Long updated_by;
	private LocalDateTime updated_dt;

	public Topic() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Topic(Long id, String topic_nm, Long topic_num, String description, Long expected_time_min, Chapter chapter,
			Long created_by, LocalDateTime created_dt, Long updated_by, LocalDateTime updated_dt) {
		super();
		this.id = id;
		this.topic_nm = topic_nm;
		this.topic_num = topic_num;
		this.description = description;
		this.expected_time_min = expected_time_min;
		this.chapter = chapter;
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

	public String getTopic_nm() {
		return topic_nm;
	}

	public void setTopic_nm(String topic_nm) {
		this.topic_nm = topic_nm;
	}

	public Long getTopic_num() {
		return topic_num;
	}

	public void setTopic_num(Long topic_num) {
		this.topic_num = topic_num;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getExpected_time_min() {
		return expected_time_min;
	}

	public void setExpected_time_min(Long expected_time_min) {
		this.expected_time_min = expected_time_min;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
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
