package com.dmantz.lms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "program_course", uniqueConstraints = {
		@UniqueConstraint(name = "uk_program_course", columnNames = { "program_id", "course_id" }) })
public class ProgramCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "program_id", referencedColumnName = "program_id", // IMPORTANT
			nullable = false)
	private Program program;

	@ManyToOne
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", // IMPORTANT
			nullable = false)
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
		return "ProgramCourse{" + "id=" + id + ", program=" + program + ", course=" + course + '}';
	}
}
