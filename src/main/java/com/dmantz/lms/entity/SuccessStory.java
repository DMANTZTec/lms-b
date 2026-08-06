package com.dmantz.lms.entity;
import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "success_story")
public class SuccessStory extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@Column(name = "placed_company", nullable = false)
	private String placedCompany;

	@Column(name = "placed_designation", nullable = false)
	private String placedDesignation;

	@Column(name = "review_msg", columnDefinition = "TEXT", nullable = false)
	private String reviewMsg;

	@Column(name = "display_order")
	private Integer displayOrder = 0;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getPlacedCompany() {
		return placedCompany;
	}

	public void setPlacedCompany(String placedCompany) {
		this.placedCompany = placedCompany;
	}

	public String getPlacedDesignation() {
		return placedDesignation;
	}

	public void setPlacedDesignation(String placedDesignation) {
		this.placedDesignation = placedDesignation;
	}

	public String getReviewMsg() {
		return reviewMsg;
	}

	public void setReviewMsg(String reviewMsg) {
		this.reviewMsg = reviewMsg;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
}