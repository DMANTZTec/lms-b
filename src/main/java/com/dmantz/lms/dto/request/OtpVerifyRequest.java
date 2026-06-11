package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

public class OtpVerifyRequest {
	private String studentId;
	private String otp;
	private OtpChannel channel;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public OtpChannel getChannel() {
		return channel;
	}

	public void setChannel(OtpChannel channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "OtpVerifyRequest{" + "studentId='" + studentId + '\'' + ", otp='" + otp + '\'' + ", channel=" + channel
				+ '}';
	}

}
