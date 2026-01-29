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

    private Long created_by;
    private LocalDateTime created_dt;

    private Long updated_by;
    private LocalDateTime updated_dt;

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

    public Long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Long created_by) {
        this.created_by = created_by;
    }

    public LocalDateTime getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(LocalDateTime created_dt) {
        this.created_dt = created_dt;
    }

    public Long getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Long updated_by) {
        this.updated_by = updated_by;
    }

    public LocalDateTime getUpdated_dt() {
        return updated_dt;
    }

    public void setUpdated_dt(LocalDateTime updated_dt) {
        this.updated_dt = updated_dt;
    }

    @Override
    public String toString() {
        return "StaffRole{" +
                "id=" + id +
                ", staff=" + staff +
                ", role=" + role +
                ", created_by=" + created_by +
                ", created_dt=" + created_dt +
                ", updated_by=" + updated_by +
                ", updated_dt=" + updated_dt +
                '}';
    }
}