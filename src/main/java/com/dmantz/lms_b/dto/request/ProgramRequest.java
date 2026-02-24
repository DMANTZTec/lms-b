package com.dmantz.lms_b.dto.request;

import com.dmantz.lms_b.entity.ProgramStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProgramRequest {

	@NotBlank(message = "Program title is required")
	@Size(max = 100, message = "Program title must not exceed 100 characters")
	private String programTitle;

	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;

	@NotNull(message = "Duration is required")
	@Min(value = 1, message = "Duration must be at least 1 month")
	private Integer durationInMonths;

	@NotNull(message = "Provider ID is required")
	private Long providerId;

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDurationInMonths() {
		return durationInMonths;
	}

	public void setDurationInMonths(Integer durationInMonths) {
		this.durationInMonths = durationInMonths;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

}
