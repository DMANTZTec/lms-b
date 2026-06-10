package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.ClassMode;
import com.dmantz.lms.entity.ClassStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClassResponse {

    private Long batchId;
    private String courseId;
    private String courseName;
    private String status;

    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalSchedulesGenerated;
    private List<ClassScheduleResponse> schedules;
    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public Integer getTotalSchedulesGenerated() {
		return totalSchedulesGenerated;
	}

	public void setTotalSchedulesGenerated(Integer totalSchedulesGenerated) {
		this.totalSchedulesGenerated = totalSchedulesGenerated;
	}

	public List<ClassScheduleResponse> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ClassScheduleResponse> schedules) {
		this.schedules = schedules;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return "ClassResponse{" +
                "batchId=" + batchId +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", className='" + className + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +

                '}';
    }
}
