package com.dmantz.lms.dto.response;

import java.time.LocalDate;

public class WeeklyClassesAttendedResponse {

    private LocalDate weekStart;
    private LocalDate weekEnd;
    private int classesAttendedCount;

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

    public int getClassesAttendedCount() {
        return classesAttendedCount;
    }

    public void setClassesAttendedCount(int classesAttendedCount) {
        this.classesAttendedCount = classesAttendedCount;
    }

    @Override
    public String toString() {
        return "WeeklyClassesAttendedResponse{" +
                "weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", classesAttendedCount=" + classesAttendedCount +
                '}';
    }
}
