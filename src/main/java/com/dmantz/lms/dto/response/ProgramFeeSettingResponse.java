package com.dmantz.lms.dto.response;

import java.util.List;

public class ProgramFeeSettingResponse {

	private String programId;
	private String programTitle;
	private String duration;

	private ProgramFeeHistoryResponse currentFee;
	private List<ProgramFeeHistoryResponse> feeHistory;
	private int totalHistoryRecords;

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public ProgramFeeHistoryResponse getCurrentFee() {
		return currentFee;
	}

	public void setCurrentFee(ProgramFeeHistoryResponse currentFee) {
		this.currentFee = currentFee;
	}

	public List<ProgramFeeHistoryResponse> getFeeHistory() {
		return feeHistory;
	}

	public void setFeeHistory(List<ProgramFeeHistoryResponse> feeHistory) {
		this.feeHistory = feeHistory;
	}

	public int getTotalHistoryRecords() {
		return totalHistoryRecords;
	}

	public void setTotalHistoryRecords(int totalHistoryRecords) {
		this.totalHistoryRecords = totalHistoryRecords;
	}
}