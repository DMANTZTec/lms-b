package com.dmantz.lms.dto.response;

import com.dmantz.lms.dto.request.ClassScheduleRequest;

import java.time.LocalDate;
import java.util.List;

public class WeeklyScheduleResponse {

    private String studentId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private Long totalClasses;
    private List<ClassScheduleResponse> classes;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    public Long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Long totalClasses) {
        this.totalClasses = totalClasses;
    }

    public List<ClassScheduleResponse> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassScheduleResponse> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "WeeklyScheduleResponse{" +
                "studentId='" + studentId + '\'' +
                ", weekStart=" + weekStart +
                ", weekEnd=" + weekEnd +
                ", totalClasses=" + totalClasses +
                ", classes=" + classes +
                '}';
    }
}
