package com.dmantz.lms_b.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "topic_reference")
public class TopicReference {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String ref_type;
private String ref_value;

private String ref_by;
private Long ref_by_id;

@ManyToOne
@JoinColumn(name = "topic_id")  // FK in TOPIC_REFERENCE table
private Topic topic;

public TopicReference() {
	super();
	// TODO Auto-generated constructor stub
}

public TopicReference(Long id, String ref_type, String ref_value, String ref_by, Long ref_by_id, Topic topic) {
	super();
	this.id = id;
	this.ref_type = ref_type;
	this.ref_value = ref_value;
	this.ref_by = ref_by;
	this.ref_by_id = ref_by_id;
	this.topic = topic;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getRef_type() {
	return ref_type;
}

public void setRef_type(String ref_type) {
	this.ref_type = ref_type;
}

public String getRef_value() {
	return ref_value;
}

public void setRef_value(String ref_value) {
	this.ref_value = ref_value;
}

public String getRef_by() {
	return ref_by;
}

public void setRef_by(String ref_by) {
	this.ref_by = ref_by;
}

public Long getRef_by_id() {
	return ref_by_id;
}

public void setRef_by_id(Long ref_by_id) {
	this.ref_by_id = ref_by_id;
}

public Topic getTopic() {
	return topic;
}

public void setTopic(Topic topic) {
	this.topic = topic;
}


}

