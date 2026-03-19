package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ="student_otp")
public class StudentOtp extends AuditFields {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(SqlTypes.CHAR) //  Tells Hibernate to store as CHAR(36)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id",
            referencedColumnName = "studentId",
            nullable = false)
    private Student student;

    @Column(name = "otp", nullable = false, length = 10)
    private String otp;

    @Column(name = "attempts_num")
    private Integer attemptsNum = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OtpStatus status = OtpStatus.NEW;        // default

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

    public OtpStatus getStatus() {
        return status;
    }

    public void setStatus(OtpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudentOtp{" +
                "id=" + id +
                ", student=" + student +
                ", otp='" + otp + '\'' +
                ", attemptsNum=" + attemptsNum +
                ", status=" + status +
                '}';
    }
}

