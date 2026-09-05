package com.dmantz.lms.entity;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_task")
public class StudentTask extends AuditFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", insertable = false, updatable = false)
	private Course course;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chapter_id")
	private Chapter chapter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "topic_id")
	private Topic topic;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false, columnDefinition = "CHAR(7)")
	private Student student;

	@Column(name = "assigned_by", nullable = false, length = 20)
	private String assignedBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "assigned_by_type", nullable = false)
	private AssignedByType assignedByType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StudentTaskStatus status;

	@Column(name = "start_dt")
	private LocalDateTime startDt;

	@Column(name = "end_dt")
	private LocalDateTime endDt;

	@Column(name = "need_help")
	private Boolean needHelp = false;

	@Column(name = "course_id", nullable = false, length = 20)
	private String courseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", insertable = false, updatable = false)
	private ClassBatch classBatch;

	@Column(name = "batch_id")
	private Long batchId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public AssignedByType getAssignedByType() {
		return assignedByType;
	}

	public void setAssignedByType(AssignedByType assignedByType) {
		this.assignedByType = assignedByType;
	}

	public StudentTaskStatus getStatus() {
		return status;
	}

	public void setStatus(StudentTaskStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartDt() {
		return startDt;
	}

	public void setStartDt(LocalDateTime startDt) {
		this.startDt = startDt;
	}

	public LocalDateTime getEndDt() {
		return endDt;
	}

	public void setEndDt(LocalDateTime endDt) {
		this.endDt = endDt;
	}

	public Boolean getNeedHelp() {
		return needHelp;
	}

	public void setNeedHelp(Boolean needHelp) {
		this.needHelp = needHelp;
	}

	public ClassBatch getClassBatch() {
		return classBatch;
	}

	public void setClassBatch(ClassBatch classBatch) {
		this.classBatch = classBatch;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

}