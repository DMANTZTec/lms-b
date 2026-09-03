package com.dmantz.lms.dto.request;

public class InstructorScheduleResponse {

    private Long id;
    private String time;
    private String date;
    private String batchName;
    private String course;
    private String className;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getClassName() {
		return className;
	}
    
    public void setClassName(String className) {
    			this.className = className;
    }
}