
package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.StudentTaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StudentTaskResponse {

	private Long id;
	private String studentId;
	private Long topicId;

	private String topicName;
	private List<Map<String, Object>> topicReferences;

	private LocalDateTime startDt;
	private LocalDateTime endDt;
	private String commitUrl;
	private StudentTaskStatus status;
	private Boolean needHelp;

	private String studentCommentTxt;
	private String reviewerCommentTxt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public List<Map<String, Object>> getTopicReferences() {
		return topicReferences;
	}

	public void setTopicReferences(List<Map<String, Object>> topicReferences) {
		this.topicReferences = topicReferences;
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
}
