package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_course")
public class StudentCourse extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many enrollments -> One student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Many enrollments -> One course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;   // PLANNED, ONGOING, COMPLETED

    private LocalDateTime start_dt;

    private LocalDateTime enrolledDt;

    private LocalDateTime completedDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public LocalDateTime getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(LocalDateTime start_dt) {
        this.start_dt = start_dt;
    }

    public LocalDateTime getEnrolledDt() {
        return enrolledDt;
    }

    public void setEnrolledDt(LocalDateTime enrolledDt) {
        this.enrolledDt = enrolledDt;
    }

    public LocalDateTime getCompletedDt() {
        return completedDt;
    }

    public void setCompletedDt(LocalDateTime completedDt) {
        this.completedDt = completedDt;
    }

    @Override
    public String toString() {
        return "StudentCourse{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course +
                ", status=" + status +
                ", start_dt=" + start_dt +
                ", enrolledDt=" + enrolledDt +
                ", completedDt=" + completedDt +
                '}';
    }
}
