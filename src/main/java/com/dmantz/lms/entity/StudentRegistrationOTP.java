package com.dmantz.lms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_registration_otp")
public class StudentRegistrationOTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_nm")
    private String firstNm;

    @Column(name = "last_nm")
    private String lastNm;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_num")
    private String mobileNum;

    @Column(name = "password")
    private String password;

    @Column(name = "current_status")
    private String currentStatus;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getFirstNm() {return firstNm;}
    public void setFirstNm(String firstNm) {this.firstNm = firstNm;}
    public String getLastNm() {return lastNm;}
    public void setLastNm(String lastNm) {this.lastNm = lastNm;}
    public String getEmailId() {return emailId;}
    public void setEmailId(String emailId) {this.emailId = emailId;}
    public String getMobileNum() {return mobileNum;}
    public void setMobileNum(String mobileNum) {this.mobileNum = mobileNum;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getCurrentStatus() {return currentStatus;}
    public void setCurrentStatus(String currentStatus) {this.currentStatus = currentStatus;}

    @Override
    public String toString() {
        return "StudentRegistrationOTP{" +
                "id=" + id +
                ", firstNm='" + firstNm + '\'' +
                ", lastNm='" + lastNm + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", password='" + password + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                '}';
    }
}
