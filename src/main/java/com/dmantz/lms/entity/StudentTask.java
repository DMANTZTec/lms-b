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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "topic_id", nullable = false)
	private Topic topic;

	@Column(name = "start_dt")
	private LocalDateTime startDt;

	@Column(name = "end_dt")
	private LocalDateTime endDt;

	@Column(name = "commit_url", length = 500)
	private String commitUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 50)
	private StudentTaskStatus status;

	@Column(name = "need_help")
	private Boolean needHelp = false;

	@Column(name = "student_comment_txt", length = 1000)
	private String studentCommentTxt;

	@Column(name = "reviewer_comment_txt", length = 1000)
	private String reviewerCommentTxt;

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

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
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

	public String getCommitUrl() {
		return commitUrl;
	}

	public void setCommitUrl(String commitUrl) {
		this.commitUrl = commitUrl;
	}

	public StudentTaskStatus getStatus() {
		return status;
	}

	public void setStatus(StudentTaskStatus status) {
		this.status = status;
	}

	public Boolean getNeedHelp() {
		return needHelp;
	}

	public void setNeedHelp(Boolean needHelp) {
		this.needHelp = needHelp;
	}

	public String getStudentCommentTxt() {
		return studentCommentTxt;
	}

	public void setStudentCommentTxt(String studentCommentTxt) {
		this.studentCommentTxt = studentCommentTxt;
	}

	public String getReviewerCommentTxt() {
		return reviewerCommentTxt;
	}

	public void setReviewerCommentTxt(String reviewerCommentTxt) {
		this.reviewerCommentTxt = reviewerCommentTxt;
	}

	@Override
	public String toString() {
		return "StudentTask{" + "id=" + id + ", student=" + student + ", topic=" + topic + ", startDt=" + startDt
				+ ", endDt=" + endDt + ", commitUrl='" + commitUrl + '\'' + ", status=" + status + ", needHelp="
				+ needHelp + ", studentCommentTxt='" + studentCommentTxt + '\'' + ", reviewerCommentTxt='"
				+ reviewerCommentTxt + '\'' + '}';
	}
}
