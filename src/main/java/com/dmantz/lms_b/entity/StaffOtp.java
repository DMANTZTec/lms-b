package com.dmantz.lms_b.entity;

import com.dmantz.lms_b.entity.base.AuditFields;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "staff_otp")
public class StaffOtp extends AuditFields {

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
    private String staffId;
    @Column(name = "otp", nullable = false, length = 10)
    private String otp;

    @Column(name = "attempts_num")
    private Integer attemptsNum = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OtpStatus status = OtpStatus.NEW;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
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

    @Override
    public String toString() {
        return "StaffOtp{" +
                "id=" + id +
                ", staffId='" + staffId + '\'' +
                ", otp='" + otp + '\'' +
                ", attemptsNum=" + attemptsNum +
                ", status=" + status +
                '}';
    }

}
