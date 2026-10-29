package com.dmantz.lms.entity;

import java.math.BigDecimal;

public class CriterionRating {

	private String criterion;
	private BigDecimal rating;

	public CriterionRating() {
	}

	public CriterionRating(String criterion, BigDecimal rating) {
		this.criterion = criterion;
		this.rating = rating;
	}

	public String getCriterion() {
		return criterion;
	}

	public void setCriterion(String criterion) {
		this.criterion = criterion;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

}
