package com.dmantz.lms_b.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "staff_role")
public class StaffRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name ="created_by")
    private Long createdBy;
    
    @Column(name ="created_dt")
    private LocalDateTime createdDt;

    @Column(name ="updated_by")
    private Long updatedBy;
    
    @Column(name ="updated_dt")
    private LocalDateTime updatedDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(LocalDateTime createdDt) {
        this.createdDt = createdDt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDt() {
        return updatedDt;
    }

    public void setUpdatedDt(LocalDateTime updatedDt) {
        this.updatedDt = updatedDt;
    }

    @Override
    public String toString() {
        return "StaffRole{" +
                "id=" + id +
                ", staff=" + staff +
                ", role=" + role +
                ", createdBy=" + createdBy +
                ", createdDt=" + createdDt +
                ", updatedBy=" + updatedBy +
                ", updatedDt=" + updatedDt +
                '}';
    }
}