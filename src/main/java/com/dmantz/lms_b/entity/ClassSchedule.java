package com.dmantz.lms_b.entity;

import com.dmantz.lms_b.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "class_schedule")
public class ClassSchedule extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @Column(nullable = false)
    private LocalDate classDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(length = 500)
    private String meetingLink;  // for online classes

    @Column(length = 500)
    private String location;     // for offline classes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassMode mode;   // ONLINE / OFFLINE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassStatus status;  // SCHEDULED / COMPLETED / CANCELLED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ClassMode getMode() {
        return mode;
    }

    public void setMode(ClassMode mode) {
        this.mode = mode;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public void setStatus(ClassStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassSchedule{" +
                "id=" + id +
                ", course=" + course +
                ", staff=" + staff +
                ", classDate=" + classDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", meetingLink='" + meetingLink + '\'' +
                ", location='" + location + '\'' +
                ", mode=" + mode +
                ", status=" + status +
                '}';
    }
}
