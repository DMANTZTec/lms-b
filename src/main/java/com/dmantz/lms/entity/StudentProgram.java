package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_program")
public class StudentProgram extends AuditFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgramEnrollmentStatus status = ProgramEnrollmentStatus.PLANNED;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

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

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public ProgramEnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(ProgramEnrollmentStatus status) {
        this.status = status;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "StudentProgram{" +
                "id=" + id +
                ", student=" + student +
                ", program=" + program +
                ", status=" + status +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
