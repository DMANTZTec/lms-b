package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

    private String studentId;
    
    @NotBlank(message = "Email or Mobile number is required")
    private String emailIdOrMobileNo;
    
    @NotBlank(message = "OTP is required")
    private String otp;
    
    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String newPassword;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	public String getEmailIdOrMobileNo() {
		return emailIdOrMobileNo;
	}

	public void setEmailIdOrMobileNo(String emailIdOrMobileNo) {
		this.emailIdOrMobileNo = emailIdOrMobileNo;
	}

    
}
