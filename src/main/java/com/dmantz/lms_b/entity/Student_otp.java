package com.dmantz.lms_b.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ="student_otp")
public class Student_otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "otp", nullable = false, length = 10)
    private String otp;

    @Column(name = "attempts_num")
    private Integer attemptsNum = 0;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Integer getAttemptsNum() {
        return attemptsNum;
    }

    public void setAttemptsNum(Integer attemptsNum) {
        this.attemptsNum = attemptsNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(LocalDateTime createdDt) {
        this.createdDt = createdDt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDt() {
        return updatedDt;
    }

    public void setUpdatedDt(LocalDateTime updatedDt) {
        this.updatedDt = updatedDt;
    }

    @Override
    public String toString() {
        return "Student_otp{" +
                "id=" + id +
                ", student=" + student +
                ", otp='" + otp + '\'' +
                ", attemptsNum=" + attemptsNum +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", createdDt=" + createdDt +
                ", updatedBy=" + updatedBy +
                ", updatedDt=" + updatedDt +
                '}';
    }
}

