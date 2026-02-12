package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "topic")
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "topic_nm")
	private String topicNm;

	@Column(name = "topic_num")
	private Long topicNum;

	@Column(name = "description")
	private String description;

	@Column(name = "expected_time_min")
	private Long expectedTimeMin;

	@ManyToOne
	@JoinColumn(name = "chapter_id")
	private Chapter chapter;
	
	@OneToMany(
	        mappedBy = "topic",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	)
	private List<TopicReference> references = new ArrayList<>();
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

	public String getTopicNm() {
		return topicNm;
	}

	public void setTopicNm(String topicNm) {
		this.topicNm = topicNm;
	}

	public Long getTopicNum() {
		return topicNum;
	}

	public void setTopicNum(Long topicNum) {
		this.topicNum = topicNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getExpectedTimeMin() {
		return expectedTimeMin;
	}

	public void setExpectedTimeMin(Long expectedTimeMin) {
		this.expectedTimeMin = expectedTimeMin;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
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

	public List<TopicReference> getReferences() {
		return references;
	}

	public void setReferences(List<TopicReference> references) {
		this.references = references;
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", topicNm=" + topicNm + ", topicNum=" + topicNum + ", description=" + description
				+ ", expectedTimeMin=" + expectedTimeMin + ", chapter=" + chapter + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + "]";
	}

}
