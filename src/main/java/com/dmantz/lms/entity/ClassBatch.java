package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class_batch")
public class ClassBatch extends AuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "class_name", length = 100, nullable = false)
    private String className;   // Batch name

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ClassStatus status;  // SCHEDULED, COMPLETED, CANCELLED

    @Column(name = "capacity")
    private Integer capacity;   // Maximum students (nullable)
    
    @ManyToMany
    @JoinTable(
            name = "class_batch_instructor",
            joinColumns = @JoinColumn(name = "batch_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "staff_id",
                    referencedColumnName = "staff_id"
            )
    )
    private Set<Staff> instructors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public void setStatus(ClassStatus status) {
        this.status = status;
    }
    

    public Set<Staff> getInstructors() {
		return instructors;
	}

	public void setInstructors(Set<Staff> instructors) {
		this.instructors = instructors;
	}

	@Override
    public String toString() {
        return "ClassBatch{" +
                "id=" + id +
                ", course=" + course +
                ", className='" + className + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
