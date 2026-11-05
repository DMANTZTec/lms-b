package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.AdminPaymentOption;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * Admin enrolls a student. Send exactly one of courseId / programId.
 *
 * CASH_RECEIVED  -> offlinePayment required
 * WAIVER         -> waiverReason required
 * PAY_LATER      -> payment request sent to the student
 * COLLECT_ONLINE -> returns Razorpay checkout details for the admin screen
 */
public class AdminEnrollmentRequest {

    @NotBlank(message = "studentId is required")
    private String studentId;

    private String courseId;

    private String programId;

    @NotNull(message = "paymentOption is required")
    private AdminPaymentOption paymentOption;

    @Valid
    private OfflinePaymentRequest offlinePayment;

    @Size(max = 255, message = "waiverReason can be at most 255 characters")
    private String waiverReason;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public AdminPaymentOption getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(AdminPaymentOption paymentOption) {
        this.paymentOption = paymentOption;
    }

    public OfflinePaymentRequest getOfflinePayment() {
        return offlinePayment;
    }

    public void setOfflinePayment(OfflinePaymentRequest offlinePayment) {
        this.offlinePayment = offlinePayment;
    }

    public String getWaiverReason() {
        return waiverReason;
    }

    public void setWaiverReason(String waiverReason) {
        this.waiverReason = waiverReason;
    }

}
