package com.dmantz.lms.dto.response;

public class BatchDetail {

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
