package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "class_student",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"class_batch_id", "student_id"})
        })
public class ClassStudent extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Class reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_batch_id", nullable = false)
    private ClassBatch classBatch;

    // Student reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ClassStudentStatus status;

    @Column(name = "enrolled_dt")
    private LocalDate enrolledDate;

    @Column(name = "start_dt")
    private LocalDate startDate;

    @Column(name = "completed_dt")
    private LocalDate completedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassBatch getClassBatch() {
        return classBatch;
    }

    public void setClassBatch(ClassBatch classBatch) {
        this.classBatch = classBatch;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassStudentStatus getStatus() {
        return status;
    }

    public void setStatus(ClassStudentStatus status) {
        this.status = status;
    }

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    @Override
    public String toString() {
        return "ClassStudent{" +
                "id=" + id +
                ", classBatch=" + classBatch +
                ", student=" + student +
                ", status=" + status +
                ", enrolledDate=" + enrolledDate +
                ", startDate=" + startDate +
                ", completedDate=" + completedDate +
                '}';
    }
}
