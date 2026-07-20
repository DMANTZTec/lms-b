package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

public class StaffUpdateRequest {

    @NotBlank
    private String firstNm;

    @NotBlank
    private String lastNm;

    private LocalDate dob;

    private Gender gender;

    private LocalDate dateOfJoining;

    private MultipartFile profileImg;

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
}
