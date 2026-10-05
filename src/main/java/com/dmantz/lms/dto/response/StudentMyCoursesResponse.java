package com.dmantz.lms.dto.response;

import java.util.List;

public class StudentMyCoursesResponse {

    private long totalCourses;
    private long planned;
    private long ongoing;
    private long completed;

    private List<MyCourseResponse> courses;


    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public long getPlanned() {
        return planned;
    }

    public void setPlanned(long planned) {
        this.planned = planned;
    }

    public long getOngoing() {
        return ongoing;
    }

    public void setOngoing(long ongoing) {
        this.ongoing = ongoing;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public List<MyCourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<MyCourseResponse> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "StudentMyCoursesResponse{" +
                "totalCourses=" + totalCourses +
                ", planned=" + planned +
                ", ongoing=" + ongoing +
                ", completed=" + completed +
                ", courses=" + courses +
                '}';
    }
}
