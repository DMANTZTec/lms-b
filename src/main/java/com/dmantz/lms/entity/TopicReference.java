package com.dmantz.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "topic_reference")
public class TopicReference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ref_type")
	private String refType;

	@Column(name = "ref_value", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> refValue;

	@Column(name = "ref_by")
	private String refBy;

	@Column(name = "ref_by_id")
	private String refById;

	@ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic topic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Map<String, Object> getRefValue() {
		return refValue;
	}

	public void setRefValue(Map<String, Object> refValue) {
		this.refValue = refValue;
	}

	public String getRefBy() {
		return refBy;
	}

	public void setRefBy(String refBy) {
		this.refBy = refBy;
	}

	public String getRefById() {
		return refById;
	}

	public void setRefById(String refById) {
		this.refById = refById;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "TopicReference [id=" + id + ", refType=" + refType + ", refValue=" + refValue + ", refBy=" + refBy
				+ ", refById=" + refById + ", topic=" + topic + "]";
	}

}