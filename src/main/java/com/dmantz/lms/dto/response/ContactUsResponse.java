package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.ContactStatus;
import com.dmantz.lms.entity.CurrentPosition;

public class ContactUsResponse {

    private Long id;
    private String fullName;
    private String mobileNumber;
    private String email;
    private CurrentPosition currentPosition;
    private String location;
    private ContactStatus status;
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CurrentPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(CurrentPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ContactUsResponse{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                ", currentPosition=" + currentPosition +
                ", location='" + location + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
