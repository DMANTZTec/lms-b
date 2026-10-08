package com.dmantz.lms.dto.response;

public class StudentClassesAttendanceResponse {

    private ClassesAttendanceStatsResponse classes;

    public ClassesAttendanceStatsResponse getClasses() {
        return classes;
    }

    public void setClasses(ClassesAttendanceStatsResponse classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "StudentClassesAttendanceResponse{" +
                "classes=" + classes +
                '}';
    }
}
