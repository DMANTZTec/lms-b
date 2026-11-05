package com.dmantz.lms.dto.response;


/*
 * enrollment is null for PAY_LATER / COLLECT_ONLINE until the student pays.
 * checkout is set only for COLLECT_ONLINE / PAY_LATER.
 */
public class AdminEnrollmentResponse {

    private EnrollmentResponse enrollment;

    private PaymentResponse payment;

    private CheckoutResponse checkout;

    public EnrollmentResponse getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(EnrollmentResponse enrollment) {
        this.enrollment = enrollment;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public CheckoutResponse getCheckout() {
        return checkout;
    }

    public void setCheckout(CheckoutResponse checkout) {
        this.checkout = checkout;
    }

}
