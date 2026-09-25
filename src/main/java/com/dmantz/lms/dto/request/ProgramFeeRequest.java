package com.dmantz.lms.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dmantz.lms.entity.ProgramDuration;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ProgramFeeRequest {

	@NotNull(message = "Effective date is required")
	private LocalDate effectiveDate;

	@NotNull(message = "Fee is required")
	@DecimalMin(value = "0.01", message = "Fee must be greater than 0")
	private BigDecimal fee;

	@DecimalMin(value = "0.00", message = "Discount cannot be negative")
	private BigDecimal discount;

	private ProgramDuration Duration;

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

	public ProgramDuration getDuration() {
		return Duration;
	}

	public void setDuration(ProgramDuration duration) {
		Duration = duration;
	}

}