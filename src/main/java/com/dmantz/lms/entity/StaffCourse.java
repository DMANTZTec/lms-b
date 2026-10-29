package com.dmantz.lms.entity;

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
@Table(name = "staff_course", uniqueConstraints = {
        @UniqueConstraint(name = "uk_staff_course", 
                columnNames = {"staff_id", "course_id"})
})
public class StaffCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "staff_id",
            referencedColumnName = "staff_id",  // ✅ String business ID in Staff entity
            nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "course_id", // ✅ String business ID in Course entity
            nullable = false)
    private Course course;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    @Override
    public String toString() {
        return "StaffCourse{" + 
                "id=" + id + 
                ", staff=" + staff + 
                ", course=" + course + '}';
    }
}
