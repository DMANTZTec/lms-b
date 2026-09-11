package com.dmantz.lms.dto.response;

public class InstructorResponse {

	private String staffId;
	private String firstNm;
	private String lastNm;
	private String designation;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getFirstNm() {
		return firstNm;
	}

	public void setFirstNm(String firstNm) {
		this.firstNm = firstNm;
	}

	public String getLastNm() {
		return lastNm;
	}

	public void setLastNm(String lastNm) {
		this.lastNm = lastNm;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "InstructorChipResponse{" + "staffId='" + staffId + '\'' + ", firstNm='" + firstNm + '\'' + ", lastNm='"
				+ lastNm + '\'' + ", designation='" + designation + '\'' + '}';
	}
}