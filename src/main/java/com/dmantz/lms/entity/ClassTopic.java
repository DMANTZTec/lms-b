package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "class_topic")
public class ClassTopic extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to ClassBatch
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_batch_id", nullable = false)
    private ClassBatch classBatch;

    // Reference to Topic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "status")
    private String status; // PLANNED, COMPLETED


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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassTopic{" +
                "id=" + id +
                ", classBatch=" + classBatch +
                ", topic=" + topic +
                ", status='" + status + '\'' +
                '}';
    }
}
