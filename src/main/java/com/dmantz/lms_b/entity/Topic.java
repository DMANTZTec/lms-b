package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dmantz.lms_b.entity.base.AuditFields;

import jakarta.persistence.*;

@Entity
@Table(name = "topic")
public class Topic extends AuditFields{

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chapter_id")
	private Chapter chapter;

	@OneToMany(
	        mappedBy = "topic",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	)
	private List<TopicReference> references = new ArrayList<>();

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

	public List<TopicReference> getReferences() {
		return references;
	}

	public void setReferences(List<TopicReference> references) {
		this.references = references;
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", topicNm=" + topicNm + ", topicNum=" + topicNum + ", description=" + description
				+ ", expectedTimeMin=" + expectedTimeMin + ", chapter=" + chapter + ", references=" + references + "]";
	}

	

}
