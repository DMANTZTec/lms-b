package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "student_task_mentor")
public class StudentTaskMentor extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_task_id", nullable = false)
    private StudentTask studentTask;

    // Mentor (also a student)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mentor_student_id", nullable = false)
    private Student mentorStudent;

    @Column(name = "mins_spent")
    private Integer minsSpent;

    @Column(name = "student_ack")
    private Boolean studentAck = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MentorHelpStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentTask getStudentTask() {
        return studentTask;
    }

    public void setStudentTask(StudentTask studentTask) {
        this.studentTask = studentTask;
    }

    public Student getMentorStudent() {
        return mentorStudent;
    }

    public void setMentorStudent(Student mentorStudent) {
        this.mentorStudent = mentorStudent;
    }

    public Integer getMinsSpent() {
        return minsSpent;
    }

    public void setMinsSpent(Integer minsSpent) {
        this.minsSpent = minsSpent;
    }

    public Boolean getStudentAck() {
        return studentAck;
    }

    public void setStudentAck(Boolean studentAck) {
        this.studentAck = studentAck;
    }

    public MentorHelpStatus getStatus() {
        return status;
    }

    public void setStatus(MentorHelpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudentTaskMentor{" +
                "id=" + id +
                ", studentTask=" + studentTask +
                ", mentorStudent=" + mentorStudent +
                ", minsSpent=" + minsSpent +
                ", studentAck=" + studentAck +
                ", status=" + status +
                '}';
    }
}
