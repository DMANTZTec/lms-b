package com.dmantz.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dmantz.lms.entity.ProgramDuration;


public class ProgramFeeHistoryResponse {

    private LocalDate effectiveDate;
    private BigDecimal fee;
    private BigDecimal discount;
    private ProgramDuration duration;
    private String durationLabel;
    private String setBy;

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public ProgramDuration getDuration() { return duration; }
    public void setDuration(ProgramDuration duration) { this.duration = duration; }

    public String getDurationLabel() { return durationLabel; }
    public void setDurationLabel(String durationLabel) { this.durationLabel = durationLabel; }

    public String getSetBy() { return setBy; }
    public void setSetBy(String setBy) { this.setBy = setBy; }
}