package com.dmantz.lms.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.dmantz.lms.entity.Country;
import com.dmantz.lms.entity.State;

import jakarta.validation.constraints.NotNull;

public class StudentUpdateRequest {
	private String firstNm;
	private String lastNm;
	private String gender;
	private String addr1;
	private String addr2;
	private String city;
	private State state;
	private String status;
	private Country country;
	private String pin;
	private String mobileNum;
	private String emergencyContactNm;
	private String emergencyContactNum;

	@NotNull(message = "Date of birth is required")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dob;

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getEmergencyContactNm() {
		return emergencyContactNm;
	}

	public void setEmergencyContactNm(String emergencyContactNm) {
		this.emergencyContactNm = emergencyContactNm;
	}

	public String getEmergencyContactNum() {
		return emergencyContactNum;
	}

	public void setEmergencyContactNum(String emergencyContactNum) {
		this.emergencyContactNum = emergencyContactNum;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "StudentUpdateRequest [firstNm=" + firstNm + ", lastNm=" + lastNm + ", gender=" + gender + ", addr1="
				+ addr1 + ", addr2=" + addr2 + ", city=" + city + ", state=" + state + ", country=" + country + ", pin="
				+ pin + ", mobileNum=" + mobileNum + ", emergencyContactNm=" + emergencyContactNm
				+ ", emergencyContactNum=" + emergencyContactNum + ", dob=" + dob + "]";
	}

}
