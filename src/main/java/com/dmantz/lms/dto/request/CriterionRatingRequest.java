package com.dmantz.lms.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CriterionRatingRequest {

	@NotBlank(message = "Criterion name is required")
	private String criterion;

	@NotNull(message = "Criterion rating is required")
	@DecimalMin(value = "0.0", message = "Criterion rating must be at least 0")
	@DecimalMax(value = "5.0", message = "Criterion rating must be at most 5")
	@Digits(integer = 1, fraction = 1, message = "Criterion rating can have at most one decimal place")
	private BigDecimal rating;

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
