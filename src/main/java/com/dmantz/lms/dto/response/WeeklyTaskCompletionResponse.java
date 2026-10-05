package com.dmantz.lms.dto.response;

import java.time.LocalDate;

public class WeeklyTaskCompletionResponse {

    private LocalDate weekStart;
    private LocalDate weekEnd;
    private int completedTaskCount;

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

    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(int completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    @Override
    public String toString() {
        return "WeeklyTaskCompletionResponse{" +
                "weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", completedTaskCount=" + completedTaskCount +
                '}';
    }
}
