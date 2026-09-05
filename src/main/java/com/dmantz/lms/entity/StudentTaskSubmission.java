package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student_task_submission")
public class StudentTaskSubmission extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_task_id", nullable = false)
    private StudentTask studentTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false, columnDefinition = "CHAR(7)")
    private Student student;

    @Column(name = "submission_notes", columnDefinition = "TEXT")
    private String submissionNotes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attachments", columnDefinition = "json")
    private List<FileAttachment> attachments;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", nullable = false, columnDefinition = "VARCHAR(20)")
    private Staff instructor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TaskSubmissionStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "review_feedback", columnDefinition = "TEXT")
    private String reviewFeedback;

    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSubmissionNotes() {
        return submissionNotes;
    }

    public void setSubmissionNotes(String submissionNotes) {
        this.submissionNotes = submissionNotes;
    }

    public List<FileAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FileAttachment> attachments) {
        this.attachments = attachments;
    }

    public Staff getInstructor() {
        return instructor;
    }

    public void setInstructor(Staff instructor) {
        this.instructor = instructor;
    }

    public TaskSubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(TaskSubmissionStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getReviewFeedback() {
        return reviewFeedback;
    }

    public void setReviewFeedback(String reviewFeedback) {
        this.reviewFeedback = reviewFeedback;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}