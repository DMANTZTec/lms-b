package com.dmantz.lms_b.dto.response;

import java.util.List;

public class StudentDashboardResponse {

    private StudentSummaryResponse summary;
    private List<StudentCourseResponse> courses;

    public StudentSummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(StudentSummaryResponse summary) {
        this.summary = summary;
    }

    public List<StudentCourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<StudentCourseResponse> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "StudentDashboardResponse{" +
                "summary=" + summary +
                ", courses=" + courses +
                '}';
    }
}
