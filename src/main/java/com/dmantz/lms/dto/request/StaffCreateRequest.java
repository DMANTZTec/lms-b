package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.Gender;
import com.dmantz.lms.entity.OtpChannel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

public class StaffCreateRequest {

    @NotBlank
    private String firstNm;

    @NotBlank
    private String lastNm;

    @Email
    @NotBlank
    private String emailId;

    @NotBlank
    private String mobileNum;

    private LocalDate dob;

    private Gender gender;

    private LocalDate dateOfJoining;

    private MultipartFile profileImg;
    
    @NotNull(message = "OTP channel is required (EMAIL or MOBILE)")
 	private OtpChannel otpChannel;

    @NotEmpty
    private Set<Long> roleIds;

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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public MultipartFile getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(MultipartFile profileImg) {
        this.profileImg = profileImg;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

	public OtpChannel getOtpChannel() {
		return otpChannel;
	}

	public void setOtpChannel(OtpChannel otpChannel) {
		this.otpChannel = otpChannel;
	}
    
    
}
