package com.dmantz.lms.dto.request;

import java.time.LocalDate;

public class UpdateClassRequest {

    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

  
}
