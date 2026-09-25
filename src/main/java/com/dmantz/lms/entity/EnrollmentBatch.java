package com.dmantz.lms.entity;

import java.time.LocalDateTime;

import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "enrollment_batch",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_enrollment_batch",
            columnNames = {
                "enrollment_id",
                "batch_id"
            }
        )
    }
)
public class EnrollmentBatch extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "enrollment_id",
        nullable = false
    )
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "batch_id",
        nullable = false
    )
    private ClassBatch classBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "assigned_by",
        referencedColumnName = "staff_id",
        nullable = false
    )
    private Staff assignedBy;

    @Column(
        name = "assigned_date",
        nullable = false
    )
    private LocalDateTime assignedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public ClassBatch getClassBatch() {
		return classBatch;
	}

	public void setClassBatch(ClassBatch classBatch) {
		this.classBatch = classBatch;
	}

	public Staff getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(Staff assignedBy) {
		this.assignedBy = assignedBy;
	}

	public LocalDateTime getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDateTime assignedDate) {
		this.assignedDate = assignedDate;
	}

    
    
}