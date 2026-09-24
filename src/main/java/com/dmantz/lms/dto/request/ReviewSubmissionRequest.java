package com.dmantz.lms.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewSubmissionRequest {

	@NotNull(message = "Overall rating is required")
	@DecimalMin(value = "0.0", message = "Overall rating must be at least 0")
	@DecimalMax(value = "5.0", message = "Overall rating must be at most 5")
	@Digits(integer = 1, fraction = 1, message = "Overall rating can have at most one decimal place")
	private BigDecimal overallRating;

	@NotBlank(message = "Feedback message is required")
	private String feedbackMessage;

	public BigDecimal getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(BigDecimal overallRating) {
		this.overallRating = overallRating;
	}

	public String getFeedbackMessage() {
		return feedbackMessage;
	}

	public void setFeedbackMessage(String feedbackMessage) {
		this.feedbackMessage = feedbackMessage;
	}
}
