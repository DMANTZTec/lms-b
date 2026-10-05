package com.dmantz.lms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_fee")
public class CourseFee extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
	private Course course;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "course_duration")
	private CourseDuration courseDuration;

	public CourseDuration getCourseDuration() {
	    return courseDuration;
	}

	public void setCourseDuration(CourseDuration courseDuration) {
	    this.courseDuration = courseDuration;
	}

	@Column(name = "effective_date", nullable = false)
	private LocalDate effectiveDate;

	@Column(name = "fee", nullable = false)
	private BigDecimal fee;

	@Column(name = "discount", nullable = false)
	private BigDecimal discount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
}