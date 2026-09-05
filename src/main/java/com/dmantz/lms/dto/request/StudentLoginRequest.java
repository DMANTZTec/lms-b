package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

public class StudentLoginRequest {

    private String username;   // email / mobile / loginId
    private String password;
    private OtpChannel otpChannel;

    public StudentLoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public OtpChannel getOtpChannel() {
		return otpChannel;
	}
    
    public void setOtpChannel(OtpChannel otpChannel) {
		this.otpChannel = otpChannel;
	}

	@Override
	public String toString() {
		return "StudentLoginRequest{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", otpChannel=" + otpChannel +
				'}';
	}

}
