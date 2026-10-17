package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "schedule_attendance",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_schedule_attendance_schedule_student",
                columnNames = {"schedule_id", "student_id"}
        )
)
public class ScheduleAttendance extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ClassSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AttendanceStatus status = AttendanceStatus.UNMARKED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by_staff_id", referencedColumnName = "staff_id")
    private Staff markedBy;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(ClassSchedule schedule) {
        this.schedule = schedule;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public Staff getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(Staff markedBy) {
        this.markedBy = markedBy;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }
}
