package com.dmantz.lms.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/*
 * amount is optional: leave it empty for a full refund.
 */
public class RefundRequest {

    @Positive(message = "amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "reason is required")
    @Size(max = 255, message = "reason can be at most 255 characters")
    private String reason;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
