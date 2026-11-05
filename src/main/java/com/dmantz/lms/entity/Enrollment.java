package com.dmantz.lms.entity;

import java.time.LocalDateTime;

import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollment")
public class Enrollment extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Student who is enrolling
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "student_id",
            nullable = false
    )
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "course_id"
    )
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "program_id",
            referencedColumnName = "program_id"
    )
    private Program program;
    /*
     * COURSE or PROGRAM
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "enrollment_type",
            nullable = false
    )
    private EnrollmentType enrollmentType;

    @Column(
            name = "enrollment_date",
            nullable = false
    )
    private LocalDateTime enrollmentDate;

    /*
     * ACTIVE / COMPLETED / CANCELLED
     * An enrollment is created only after its payment is settled.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private EnrollmentStatus status;

    /*
     * Summary of how the fee was settled, kept for existing API consumers:
     * PAID / WAIVED / FREE / REFUNDED. The payment table is the source of truth.
     */
    @Column(
            name = "payment_status",
            nullable = false
    )
    private String paymentStatus;

    /*
     * SELF (student checkout) or ADMIN (enrolled by admin)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private EnrollmentSource source;


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

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public EnrollmentType getEnrollmentType() {
        return enrollmentType;
    }

    public void setEnrollmentType(EnrollmentType enrollmentType) {
        this.enrollmentType = enrollmentType;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public EnrollmentSource getSource() {
        return source;
    }

    public void setSource(EnrollmentSource source) {
        this.source = source;
    }
}
