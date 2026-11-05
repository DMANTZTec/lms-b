package com.dmantz.lms.dto.response;

import java.time.LocalDate;

public class WeeklyHoursSpentResponse {

    private LocalDate weekStart;
    private LocalDate weekEnd;
    private double totalHours;

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    @Override
    public String toString() {
        return "WeeklyHoursSpentResponse{" +
                "weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", totalHours=" + totalHours +
                '}';
    }
}
