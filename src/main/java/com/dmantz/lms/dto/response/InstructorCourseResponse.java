package com.dmantz.lms.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstructorCourseResponse {

    private String courseId;
    private String title;
    private String subject;
    private String description;
    private String level;
    private String language;
    private String status;
    private double progress;
    private int totalStudents;
    private int activeStudents;
    private int chaptersCompleted;
    private int totalChapters;
    private int upcomingClasses;
    private String courseImage;
    private LocalDate lastClassDate;
    private LocalDate nextClassDate;
    private List<String> skills = new ArrayList<>();
    private List<BatchDetail> batches = new ArrayList<>();

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(int activeStudents) {
        this.activeStudents = activeStudents;
    }

    public int getChaptersCompleted() {
        return chaptersCompleted;
    }

    public void setChaptersCompleted(int chaptersCompleted) {
        this.chaptersCompleted = chaptersCompleted;
    }

    public int getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public int getUpcomingClasses() {
        return upcomingClasses;
    }

    public void setUpcomingClasses(int upcomingClasses) {
        this.upcomingClasses = upcomingClasses;
    }

    public LocalDate getLastClassDate() {
        return lastClassDate;
    }

    public void setLastClassDate(LocalDate lastClassDate) {
        this.lastClassDate = lastClassDate;
    }

    public LocalDate getNextClassDate() {
        return nextClassDate;
    }

    public void setNextClassDate(LocalDate nextClassDate) {
        this.nextClassDate = nextClassDate;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<BatchDetail> getBatches() {
        return batches;
    }

    public void setBatches(List<BatchDetail> batches) {
        this.batches = batches;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    @Override
    public String toString() {
        return "InstructorCourseResponse{" +
                "courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", language='" + language + '\'' +
                ", status='" + status + '\'' +
                ", progress=" + progress +
                ", totalStudents=" + totalStudents +
                ", activeStudents=" + activeStudents +
                ", chaptersCompleted=" + chaptersCompleted +
                ", totalChapters=" + totalChapters +
                ", upcomingClasses=" + upcomingClasses +
                ", courseImage='" + courseImage + '\'' +
                ", lastClassDate=" + lastClassDate +
                ", nextClassDate=" + nextClassDate +
                ", skills=" + skills +
                ", batches=" + batches +
                '}';
    }

    public static class BatchDetail {

        private String name;
        private int students;
        private String schedule;

        public BatchDetail() {
        }

        public BatchDetail(String name, int students, String schedule) {
            this.name = name;
            this.students = students;
            this.schedule = schedule;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStudents() {
            return students;
        }

        public void setStudents(int students) {
            this.students = students;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }
    }
}
