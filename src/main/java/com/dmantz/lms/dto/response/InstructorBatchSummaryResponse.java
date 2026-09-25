package com.dmantz.lms.dto.response;

import java.util.List;

public class InstructorBatchSummaryResponse {

	private int activeBatchCount;
	private int completedBatchCount;
	private List<InstructorBatchResponse> activeBatches;
	private List<InstructorBatchResponse> completedBatches;

	public InstructorBatchSummaryResponse() {
	}

	public InstructorBatchSummaryResponse(int activeBatchCount, int completedBatchCount,
			List<InstructorBatchResponse> activeBatches, List<InstructorBatchResponse> completedBatches) {
		this.activeBatchCount = activeBatchCount;
		this.completedBatchCount = completedBatchCount;
		this.activeBatches = activeBatches;
		this.completedBatches = completedBatches;
	}

	public int getActiveBatchCount() {
		return activeBatchCount;
	}

	public void setActiveBatchCount(int activeBatchCount) {
		this.activeBatchCount = activeBatchCount;
	}

	public int getCompletedBatchCount() {
		return completedBatchCount;
	}

	public void setCompletedBatchCount(int completedBatchCount) {
		this.completedBatchCount = completedBatchCount;
	}

	public List<InstructorBatchResponse> getActiveBatches() {
		return activeBatches;
	}

	public void setActiveBatches(List<InstructorBatchResponse> activeBatches) {
		this.activeBatches = activeBatches;
	}

	public List<InstructorBatchResponse> getCompletedBatches() {
		return completedBatches;
	}

	public void setCompletedBatches(List<InstructorBatchResponse> completedBatches) {
		this.completedBatches = completedBatches;
	}
}
