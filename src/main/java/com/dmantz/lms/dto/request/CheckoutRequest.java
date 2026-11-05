package com.dmantz.lms.dto.request;


/*
 * Student checkout. Send exactly one of courseId / programId.
 * The student is taken from the JWT, never from the body.
 */
public class CheckoutRequest {

    private String courseId;

    private String programId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

}
