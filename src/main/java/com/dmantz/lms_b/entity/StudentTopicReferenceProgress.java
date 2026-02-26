package com.dmantz.lms_b.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_topic_reference_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_topic_reference",
                        columnNames = {"student_id", "topic_reference_id"}
                )
        }
)
public class StudentTopicReferenceProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_reference_id", nullable = false)
    private TopicReference topicReference;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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

    public TopicReference getTopicReference() {
        return topicReference;
    }

    public void setTopicReference(TopicReference topicReference) {
        this.topicReference = topicReference;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "StudentTopicReferenceProgress{" +
                "id=" + id +
                ", student=" + student +
                ", topicReference=" + topicReference +
                ", completed=" + completed +
                ", completedAt=" + completedAt +
                '}';
    }
}
