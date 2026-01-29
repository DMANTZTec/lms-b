package com.dmantz.lms_b.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "staff_otp")
public class StaffOtp {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(SqlTypes.CHAR) // Store UUID as CHAR(36)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    // Difference: No @ManyToOne, direct staff_id reference
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "otp", nullable = false, length = 10)
    private String otp;

    @Column(name = "attempts_num")
    private Integer attemptsNum = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OtpStatus status = OtpStatus.NEW;

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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public OtpStatus getStatus() {
        return status;
    }

    public void setStatus(OtpStatus status) {
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
        return "StaffOtp{" +
                "id=" + id +
                ", staffId=" + staffId +
                ", otp='" + otp + '\'' +
                ", attemptsNum=" + attemptsNum +
                ", status=" + status +
                ", createdBy=" + createdBy +
                ", createdDt=" + createdDt +
                ", updatedBy=" + updatedBy +
                ", updatedDt=" + updatedDt +
                '}';
    }
}
