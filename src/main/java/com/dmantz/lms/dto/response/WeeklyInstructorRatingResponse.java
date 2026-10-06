package com.dmantz.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WeeklyInstructorRatingResponse {

    private LocalDate weekStart;
    private LocalDate weekEnd;
    private BigDecimal averageRating;
    private int ratedTaskCount;

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

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatedTaskCount() {
        return ratedTaskCount;
    }

    public void setRatedTaskCount(int ratedTaskCount) {
        this.ratedTaskCount = ratedTaskCount;
    }

    @Override
    public String toString() {
        return "WeeklyInstructorRatingResponse{" +
                "weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", averageRating=" + averageRating +
                ", ratedTaskCount=" + ratedTaskCount +
                '}';
    }
}
