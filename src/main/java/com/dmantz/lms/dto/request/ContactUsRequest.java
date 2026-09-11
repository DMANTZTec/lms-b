package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.CurrentPosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ContactUsRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String mobileNumber;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private CurrentPosition currentPosition;

    @NotNull
    private String location;

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

    @Override
    public String toString() {
        return "ContactUsRequest{" +
                "fullName='" + fullName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                ", currentPosition=" + currentPosition +
                ", location='" + location + '\'' +
                '}';
    }
}
