package com.dmantz.lms.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dmantz.lms.entity.CourseDuration;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CourseFeeRequest {

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @NotNull(message = "Fee is required")
    @DecimalMin(value = "0.01", message = "Fee must be greater than 0")
    private BigDecimal fee;

    @DecimalMin(value = "0.00", message = "Discount cannot be negative")
    private BigDecimal discount;

    private CourseDuration courseDuration;

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
}