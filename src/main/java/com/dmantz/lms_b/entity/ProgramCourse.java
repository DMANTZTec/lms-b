package com.dmantz.lms_b.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "program_course",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_program_course",
                        columnNames = {"program_id", "course_id"}
                )}
)
public class ProgramCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many courses can belong to one program
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    // Many programs can have one course
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "ProgramCourse{" +
                "id=" + id +
                ", program=" + program +
                ", course=" + course +
                '}';
    }
}

