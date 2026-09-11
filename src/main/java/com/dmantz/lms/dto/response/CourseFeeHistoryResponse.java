package com.dmantz.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dmantz.lms.entity.CourseDuration;


public class CourseFeeHistoryResponse {
	
	private int serialNumber;
    private LocalDate effectiveDate;
    private BigDecimal fee;
    private BigDecimal discount;
    private CourseDuration courseDuration;
    private String courseDurationLabel;
    

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

    public CourseDuration getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(CourseDuration courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseDurationLabel() {
        return courseDurationLabel;
    }

    public void setCourseDurationLabel(String courseDurationLabel) {
        this.courseDurationLabel = courseDurationLabel;
    }

	public int getSerialNumber() {
		return serialNumber;
		
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
}