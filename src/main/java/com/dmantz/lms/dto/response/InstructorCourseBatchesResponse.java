package com.dmantz.lms.dto.response;

import java.util.ArrayList;
import java.util.List;

public class InstructorCourseBatchesResponse {

	private String courseId;
	private String courseName;
	private List<Batch> batches = new ArrayList<>();

	public InstructorCourseBatchesResponse() {
	}

	public InstructorCourseBatchesResponse(String courseId, String courseName) {
		this.courseId = courseId;
		this.courseName = courseName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}

	public static class Batch {

		private Long batchId;
		private String batchName;

		public Batch() {
		}

		public Batch(Long batchId, String batchName) {
			this.batchId = batchId;
			this.batchName = batchName;
		}

		public Long getBatchId() {
			return batchId;
		}

		public void setBatchId(Long batchId) {
			this.batchId = batchId;
		}

		public String getBatchName() {
			return batchName;
		}

		public void setBatchName(String batchName) {
			this.batchName = batchName;
		}
	}
}
