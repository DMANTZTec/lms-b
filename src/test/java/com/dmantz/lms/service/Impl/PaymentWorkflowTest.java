package com.dmantz.lms.service.Impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dmantz.lms.dto.request.AdminEnrollmentRequest;
import com.dmantz.lms.dto.request.OfflinePaymentRequest;
import com.dmantz.lms.dto.request.PaymentActionRequest;
import com.dmantz.lms.dto.request.RefundRequest;
import com.dmantz.lms.dto.response.AdminEnrollmentResponse;
import com.dmantz.lms.entity.AdminPaymentOption;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.CourseFee;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentSource;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.EnrollmentType;
import com.dmantz.lms.entity.OfflinePaymentMode;
import com.dmantz.lms.entity.Payment;
import com.dmantz.lms.entity.PaymentMethod;
import com.dmantz.lms.entity.PaymentStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.DuplicateEnrollmentException;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.mapper.EnrollmentMapper;
import com.dmantz.lms.repository.CourseFeeRepository;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.PaymentRepository;
import com.dmantz.lms.repository.ProgramFeeRepository;
import com.dmantz.lms.repository.ProgramRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.impl.AdminPaymentServiceImpl;
import com.dmantz.lms.service.impl.PaymentWorkflow;
import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.PaymentClient;
import com.razorpay.RazorpayClient;
import com.razorpay.Refund;

/*
 * Payment workflow: enrollment is created only when a payment is PAID.
 */
public class PaymentWorkflowTest {

    private static final String STUDENT_ID = "STU0104";
    private static final String COURSE_ID = "C101";
    private static final Long ADMIN_STAFF_ID = 7L;

    @Mock private PaymentRepository paymentRepository;
    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private EnrollmentBatchRepository enrollmentBatchRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private ProgramRepository programRepository;
    @Mock private CourseFeeRepository courseFeeRepository;
    @Mock private ProgramFeeRepository programFeeRepository;
    @Mock private EnrollmentMapper enrollmentMapper;
    @Mock private EmailService emailService;

    private RazorpayClient razorpayClient;
    private OrderClient orderClient;
    private PaymentClient paymentClient;

    private PaymentWorkflow workflow;
    private AdminPaymentServiceImpl adminService;

    private Student student;
    private Course course;
    private AutoCloseable mocks;
    private long nextId;

    @BeforeMethod
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        nextId = 100;

        razorpayClient = mock(RazorpayClient.class);
        orderClient = mock(OrderClient.class);
        paymentClient = mock(PaymentClient.class);
        razorpayClient.orders = orderClient;
        razorpayClient.payments = paymentClient;

        workflow = new PaymentWorkflow(paymentRepository, enrollmentRepository, enrollmentBatchRepository,
                studentRepository, courseRepository, programRepository, courseFeeRepository,
                programFeeRepository, razorpayClient);
        ReflectionTestUtils.setField(workflow, "keyId", "rzp_test_key");
        ReflectionTestUtils.setField(workflow, "currency", "INR");

        adminService = new AdminPaymentServiceImpl(workflow, paymentRepository, enrollmentMapper, emailService);
        ReflectionTestUtils.setField(adminService, "studentOrderExpiryHours", 24);
        ReflectionTestUtils.setField(adminService, "adminRequestExpiryHours", 72);

        student = new Student();
        student.setId(1L);
        student.setStudentId(STUDENT_ID);
        student.setFirstNm("Kiran");
        student.setLastNm("Rao");
        student.setEmailId("kiran@example.com");

        course = new Course();
        course.setId(11L);
        course.setCourseId(COURSE_ID);
        course.setCourseTitle("Full Stack Java with Spring Boot");

        CourseFee fee = new CourseFee();
        fee.setFee(new BigDecimal("24999"));
        fee.setDiscount(new BigDecimal("5000"));

        when(studentRepository.findByStudentIdForUpdate(STUDENT_ID)).thenReturn(Optional.of(student));
        when(courseRepository.findByCourseIdAndIsDeletedFalse(COURSE_ID)).thenReturn(Optional.of(course));
        when(courseFeeRepository.findTopByCourse_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                eq(11L), any(LocalDate.class))).thenReturn(Optional.of(fee));
        when(enrollmentRepository.findFirstByStudentStudentIdAndCourseCourseIdAndStatusNot(
                STUDENT_ID, COURSE_ID, EnrollmentStatus.CANCELLED)).thenReturn(Optional.empty());
        when(paymentRepository.findByStudent_StudentIdAndCourse_CourseIdAndStatusInOrderByIdDesc(
                anyString(), anyString(), any())).thenReturn(List.of());

        // Simulate IDENTITY ids on save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            if (p.getId() == null) {
                p.setId(nextId++);
            }
            return p;
        });
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            if (e.getId() == null) {
                e.setId(nextId++);
            }
            return e;
        });

        loginAsAdmin();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        mocks.close();
    }

    // ------------------------------------------------------------
    // Admin: cash received
    // ------------------------------------------------------------

    @Test
    public void cashReceived_createsPaidOfflinePaymentAndActiveEnrollmentImmediately() throws Exception {

        AdminEnrollmentResponse response = adminService.enroll(cashRequest(new BigDecimal("19999"), null));

        Payment payment = savedPayment();
        Assert.assertEquals(payment.getMethod(), PaymentMethod.OFFLINE);
        Assert.assertEquals(payment.getStatus(), PaymentStatus.PAID);
        Assert.assertEquals(payment.getOfflineMode(), OfflinePaymentMode.CASH);
        Assert.assertEquals(payment.getAmount(), new BigDecimal("19999.00"));
        Assert.assertEquals(payment.getFeeAmount(), new BigDecimal("24999.00"));
        Assert.assertEquals(payment.getRecordedBy(), ADMIN_STAFF_ID);
        Assert.assertTrue(payment.getReferenceNo().startsWith("RCPT-"), "receipt number generated for cash");

        Enrollment enrollment = payment.getEnrollment();
        Assert.assertNotNull(enrollment, "enrollment created");
        Assert.assertEquals(enrollment.getStatus(), EnrollmentStatus.ACTIVE);
        Assert.assertEquals(enrollment.getSource(), EnrollmentSource.ADMIN);
        Assert.assertEquals(enrollment.getPaymentStatus(), "PAID");
        Assert.assertEquals(enrollment.getEnrollmentType(), EnrollmentType.COURSE);
        Assert.assertNull(response.getCheckout(), "no gateway for cash");
        verify(orderClient, never()).create(any(JSONObject.class));
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void cashReceived_rejectsAmountDifferentFromFee() {
        adminService.enroll(cashRequest(new BigDecimal("15000"), null));
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void cashReceived_rejectsReusedReceiptNumber() {
        when(paymentRepository.existsByReferenceNo("RCPT-2026-0142")).thenReturn(true);
        adminService.enroll(cashRequest(new BigDecimal("19999"), "RCPT-2026-0142"));
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void bankTransfer_requiresReferenceNumber() {
        AdminEnrollmentRequest request = cashRequest(new BigDecimal("19999"), null);
        request.getOfflinePayment().setMode(OfflinePaymentMode.BANK_TRANSFER);
        adminService.enroll(request);
    }

    @Test(expectedExceptions = DuplicateEnrollmentException.class)
    public void enroll_rejectsStudentAlreadyEnrolled() {
        Enrollment existing = new Enrollment();
        existing.setId(5L);
        when(enrollmentRepository.findFirstByStudentStudentIdAndCourseCourseIdAndStatusNot(
                STUDENT_ID, COURSE_ID, EnrollmentStatus.CANCELLED)).thenReturn(Optional.of(existing));

        adminService.enroll(cashRequest(new BigDecimal("19999"), null));
    }

    // ------------------------------------------------------------
    // Admin: waiver
    // ------------------------------------------------------------

    @Test
    public void waiver_createsZeroPaymentWithReasonAndEnrollment() {

        AdminEnrollmentRequest request = baseRequest(AdminPaymentOption.WAIVER);
        request.setWaiverReason("Merit scholarship");

        adminService.enroll(request);

        Payment payment = savedPayment();
        Assert.assertEquals(payment.getMethod(), PaymentMethod.WAIVER);
        Assert.assertEquals(payment.getStatus(), PaymentStatus.PAID);
        Assert.assertEquals(payment.getAmount().signum(), 0);
        Assert.assertEquals(payment.getRemarks(), "Merit scholarship");
        Assert.assertEquals(payment.getEnrollment().getPaymentStatus(), "WAIVED");
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void waiver_requiresReason() {
        adminService.enroll(baseRequest(AdminPaymentOption.WAIVER));
    }

    // ------------------------------------------------------------
    // Admin: pay later -> no enrollment until paid
    // ------------------------------------------------------------

    @Test
    public void payLater_createsOnlineOrderWithoutEnrollment() throws Exception {

        Order order = mock(Order.class);
        when(order.get("id")).thenReturn("order_TEST123");
        when(orderClient.create(any(JSONObject.class))).thenReturn(order);

        AdminEnrollmentResponse response = adminService.enroll(baseRequest(AdminPaymentOption.PAY_LATER));

        Payment payment = savedPayment();
        Assert.assertEquals(payment.getMethod(), PaymentMethod.ONLINE);
        Assert.assertEquals(payment.getStatus(), PaymentStatus.CREATED);
        Assert.assertEquals(payment.getRazorpayOrderId(), "order_TEST123");
        Assert.assertNull(payment.getEnrollment(), "no enrollment before payment");
        Assert.assertTrue(payment.getExpiresAt().isAfter(LocalDateTime.now().plusHours(71)));
        Assert.assertNull(response.getEnrollment());
        Assert.assertEquals(response.getCheckout().getAmountInPaise(), Long.valueOf(1999900));
        verify(enrollmentRepository, never()).save(any());
        verify(emailService).sendPaymentRequestEmail(eq("kiran@example.com"), anyString(), anyString(),
                any(), any());
    }

    // ------------------------------------------------------------
    // Online payment settlement
    // ------------------------------------------------------------

    @Test
    public void onlinePaid_createsEnrollment_andSecondNotificationIsIgnored() {

        loginAsStudent();
        Payment payment = onlineRequest();

        workflow.markOnlinePaid(payment, "pay_ABC", "sig");
        Enrollment first = payment.getEnrollment();

        Assert.assertEquals(payment.getStatus(), PaymentStatus.PAID);
        Assert.assertNotNull(first);
        Assert.assertEquals(first.getSource(), EnrollmentSource.SELF);

        // webhook after verify
        workflow.markOnlinePaid(payment, "pay_ABC", null);
        Assert.assertSame(payment.getEnrollment(), first);
        verify(enrollmentRepository, org.mockito.Mockito.times(1)).save(any());
    }

    @Test
    public void onlinePaid_whenAlreadyEnrolled_refundsInsteadOfDuplicateEnrollment() throws Exception {

        Enrollment existing = new Enrollment();
        existing.setId(5L);
        when(enrollmentRepository.findFirstByStudentStudentIdAndCourseCourseIdAndStatusNot(
                STUDENT_ID, COURSE_ID, EnrollmentStatus.CANCELLED)).thenReturn(Optional.of(existing));
        Refund refund = mock(Refund.class);
        when(refund.get("id")).thenReturn("rfnd_1");
        when(paymentClient.refund(eq("pay_DUP"), any(JSONObject.class))).thenReturn(refund);

        Payment payment = onlineRequest();
        workflow.markOnlinePaid(payment, "pay_DUP", null);

        Assert.assertEquals(payment.getStatus(), PaymentStatus.REFUND_PENDING);
        Assert.assertEquals(payment.getRazorpayRefundId(), "rfnd_1");
        Assert.assertNull(payment.getEnrollment());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    public void failedAttempt_neverOverridesPaid() {

        Payment payment = onlineRequest();
        workflow.markOnlinePaid(payment, "pay_OK", null);
        workflow.markOnlineFailed(payment, "pay_LATE_FAIL", "declined");

        Assert.assertEquals(payment.getStatus(), PaymentStatus.PAID);
    }

    // ------------------------------------------------------------
    // Refund
    // ------------------------------------------------------------

    @Test
    public void fullOfflineRefund_cancelsEnrollment() {

        adminService.enroll(cashRequest(new BigDecimal("19999"), "RCPT-9"));
        Payment payment = savedPayment();
        when(paymentRepository.findByIdForUpdate(payment.getId())).thenReturn(Optional.of(payment));
        when(enrollmentBatchRepository.findByEnrollmentId(anyLong())).thenReturn(List.of());

        RefundRequest refund = new RefundRequest();
        refund.setReason("Student withdrew");
        adminService.refund(payment.getId(), refund);

        Assert.assertEquals(payment.getStatus(), PaymentStatus.REFUNDED);
        Assert.assertEquals(payment.getEnrollment().getStatus(), EnrollmentStatus.CANCELLED);
        Assert.assertEquals(payment.getEnrollment().getPaymentStatus(), "REFUNDED");
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void waiverCannotBeRefunded() {

        AdminEnrollmentRequest request = baseRequest(AdminPaymentOption.WAIVER);
        request.setWaiverReason("Scholarship");
        adminService.enroll(request);
        Payment payment = savedPayment();
        when(paymentRepository.findByIdForUpdate(payment.getId())).thenReturn(Optional.of(payment));

        RefundRequest refund = new RefundRequest();
        refund.setReason("test");
        adminService.refund(payment.getId(), refund);
    }

    @Test
    public void cancelRequest_marksPendingOrderCancelled() {

        Payment payment = onlineRequest();
        when(paymentRepository.findByIdForUpdate(payment.getId())).thenReturn(Optional.of(payment));

        PaymentActionRequest cancel = new PaymentActionRequest();
        cancel.setReason("Student changed mind");
        adminService.cancelRequest(payment.getId(), cancel);

        Assert.assertEquals(payment.getStatus(), PaymentStatus.CANCELLED);
    }

    // ------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------

    private Payment onlineRequest() {
        Payment payment = new Payment();
        payment.setId(nextId++);
        payment.setStudent(student);
        payment.setEnrollmentType(EnrollmentType.COURSE);
        payment.setCourse(course);
        payment.setMethod(PaymentMethod.ONLINE);
        payment.setStatus(PaymentStatus.CREATED);
        payment.setAmount(new BigDecimal("19999.00"));
        payment.setCurrency("INR");
        payment.setRazorpayOrderId("order_X");
        payment.setInitiatedBy(student.getEmailId());
        payment.setExpiresAt(LocalDateTime.now().plusHours(24));
        return payment;
    }

    private AdminEnrollmentRequest baseRequest(AdminPaymentOption option) {
        AdminEnrollmentRequest request = new AdminEnrollmentRequest();
        request.setStudentId(STUDENT_ID);
        request.setCourseId(COURSE_ID);
        request.setPaymentOption(option);
        return request;
    }

    private AdminEnrollmentRequest cashRequest(BigDecimal amount, String referenceNo) {
        OfflinePaymentRequest offline = new OfflinePaymentRequest();
        offline.setMode(OfflinePaymentMode.CASH);
        offline.setAmount(amount);
        offline.setReferenceNo(referenceNo);
        AdminEnrollmentRequest request = baseRequest(AdminPaymentOption.CASH_RECEIVED);
        request.setOfflinePayment(offline);
        return request;
    }

    private Payment savedPayment() {
        org.mockito.ArgumentCaptor<Payment> captor = org.mockito.ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, org.mockito.Mockito.atLeastOnce()).save(captor.capture());
        return captor.getValue();
    }

    private void loginAsAdmin() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "admin@dmantz.com", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        auth.setDetails(ADMIN_STAFF_ID);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void loginAsStudent() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                "kiran@example.com", null, List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))));
    }
}
