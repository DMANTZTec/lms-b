package com.dmantz.lms.dto.request;

import java.math.BigDecimal;

import com.dmantz.lms.entity.OfflinePaymentMode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/*
 * Money collected by the admin outside the gateway.
 * referenceNo is the receipt / UTR / cheque / UPI transaction number.
 * For CASH it may be left blank and the server generates a receipt number.
 */
public class OfflinePaymentRequest {

    @NotNull(message = "mode is required")
    private OfflinePaymentMode mode;

    @NotNull(message = "amount is required")
    @PositiveOrZero(message = "amount cannot be negative")
    private BigDecimal amount;

    @Size(max = 100, message = "referenceNo can be at most 100 characters")
    private String referenceNo;

    @Size(max = 255, message = "remarks can be at most 255 characters")
    private String remarks;

    public OfflinePaymentMode getMode() {
        return mode;
    }

    public void setMode(OfflinePaymentMode mode) {
        this.mode = mode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
