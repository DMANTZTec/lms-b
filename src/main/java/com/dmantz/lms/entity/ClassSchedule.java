package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class_schedule")
public class ClassSchedule extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassBatch classBatch;

    // Legacy single-instructor column — superseded by `instructors` below.
    // Kept only for backward compatibility; no longer written to.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = true)
    private Staff staff;

    @ManyToMany
    @JoinTable(
            name = "class_schedule_instructor",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "staff_id",
                    referencedColumnName = "staff_id"
            )
    )
    private Set<Staff> instructors = new HashSet<>();

    @Column(nullable = false)
    private String className;

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

    public ClassBatch getClassBatch() {
        return classBatch;
    }

    public void setClassBatch(ClassBatch classBatch) {
        this.classBatch = classBatch;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Set<Staff> getInstructors() {
        return instructors;
    }

    public void setInstructors(Set<Staff> instructors) {
        this.instructors = instructors;
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

    public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
    public String toString() {
        return "ClassSchedule{" +
                "id=" + id +
                ", classBatch=" + classBatch +
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
