package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.SendStudentNotificationRequest;
import com.dmantz.lms.dto.response.StudentNotificationResponse;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.NotificationChannel;
import com.dmantz.lms.entity.NotificationEmailStatus;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentNotification;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.UnauthorizedAccessException;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.StaffCourseRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StudentNotificationRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.StudentNotificationService;

@Service
public class StudentNotificationServiceImpl implements StudentNotificationService {

	private static final Logger logger = LogManager.getLogger(StudentNotificationServiceImpl.class);

	private final StudentNotificationRepository notificationRepository;
	private final StaffRepository staffRepository;
	private final StudentRepository studentRepository;
	private final ClassBatchRepository classBatchRepository;
	private final EnrollmentBatchRepository enrollmentBatchRepository;
	private final StaffCourseRepository staffCourseRepository;
	private final EmailService emailService;

	public StudentNotificationServiceImpl(StudentNotificationRepository notificationRepository,
			StaffRepository staffRepository, StudentRepository studentRepository,
			ClassBatchRepository classBatchRepository, EnrollmentBatchRepository enrollmentBatchRepository,
			StaffCourseRepository staffCourseRepository, EmailService emailService) {
		this.notificationRepository = notificationRepository;
		this.staffRepository = staffRepository;
		this.studentRepository = studentRepository;
		this.classBatchRepository = classBatchRepository;
		this.enrollmentBatchRepository = enrollmentBatchRepository;
		this.staffCourseRepository = staffCourseRepository;
		this.emailService = emailService;
	}

	@Override
	@Transactional
	public StudentNotificationResponse sendNotification(String instructorId, SendStudentNotificationRequest request) {

		Staff instructor = staffRepository.findByStaffId(instructorId)
				.orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + instructorId));

		Student student = studentRepository.findByStudentId(request.getStudentId().trim())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

		ClassBatch batch = classBatchRepository.findById(request.getBatchId())
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + request.getBatchId()));

		// Unproxy: student_notification.course_id references course.course_id (not the PK), and Hibernate
		// reads that value straight from the entity's field, which is always null on a lazy proxy.
		Course course = (Course) Hibernate.unproxy(batch.getCourse());
		if (course == null || !course.getCourseId().equalsIgnoreCase(request.getCourseId().trim())) {
			throw new IllegalArgumentException(
					"Batch " + request.getBatchId() + " does not belong to course " + request.getCourseId());
		}

		// Instructor must teach this batch, or at least be assigned to its course.
		boolean instructorAllowed = classBatchRepository.existsByIdAndInstructorsStaffId(batch.getId(),
				instructor.getStaffId())
				|| staffCourseRepository.existsByStaff_StaffIdAndCourse_CourseId(instructor.getStaffId(),
						course.getCourseId());
		if (!instructorAllowed) {
			throw new UnauthorizedAccessException("Instructor " + instructor.getStaffId()
					+ " is not assigned to batch " + batch.getId() + " / course " + course.getCourseId());
		}

		if (!enrollmentBatchRepository.existsByEnrollment_Student_StudentIdAndClassBatch_Id(student.getStudentId(),
				batch.getId())) {
			throw new IllegalArgumentException(
					"Student " + student.getStudentId() + " is not enrolled in batch " + batch.getId());
		}

		// No channel selected -> deliver through every channel (in-app + email).
		Set<NotificationChannel> channels = request.getChannels() == null || request.getChannels().isEmpty()
				? new LinkedHashSet<>(List.of(NotificationChannel.values()))
				: new LinkedHashSet<>(request.getChannels());

		StudentNotification notification = new StudentNotification();
		notification.setStudent(student);
		notification.setStaff(instructor);
		notification.setClassBatch(batch);
		notification.setCourse(course);
		notification.setMessage(request.getMessage().trim());
		notification.setChannels(channels.stream().map(Enum::name).collect(Collectors.joining(",")));
		notification.setSentAt(LocalDateTime.now());

		// A failed email must not lose the in-app message, so email errors are recorded, not thrown.
		notification.setEmailStatus(channels.contains(NotificationChannel.EMAIL)
				? sendEmail(student, instructor, course, batch, notification.getMessage())
				: NotificationEmailStatus.NOT_REQUESTED);

		// Only in-app messages are stored; they are what the student dashboard shows.
		// Email-only messages are just mailed, so the response has no notificationId.
		if (channels.contains(NotificationChannel.IN_APP)) {
			notification = notificationRepository.save(notification);
		}

		logger.info("Instructor {} sent notification {} to student {} via {} (email: {})", instructor.getStaffId(),
				notification.getId(), student.getStudentId(), notification.getChannels(),
				notification.getEmailStatus());

		return toResponse(notification);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudentNotificationResponse> getStudentNotifications(String studentId) {

		if (!studentRepository.existsByStudentId(studentId)) {
			throw new ResourceNotFoundException("Student not found: " + studentId);
		}

		return notificationRepository.findInAppForStudent(studentId).stream().map(this::toResponse).toList();
	}

	private NotificationEmailStatus sendEmail(Student student, Staff instructor, Course course, ClassBatch batch,
			String message) {
		if (student.getEmailId() == null || student.getEmailId().isBlank()) {
			logger.warn("Student {} has no email id; skipping notification email", student.getStudentId());
			return NotificationEmailStatus.FAILED;
		}
		try {
			emailService.sendStudentNotificationEmail(student.getEmailId(),
					fullName(student.getFirstNm(), student.getLastNm()),
					fullName(instructor.getFirstNm(), instructor.getLastNm()), course.getCourseTitle(),
					batch.getClassName(), message);
			return NotificationEmailStatus.SENT;
		} catch (Exception ex) {
			logger.error("Notification email to student {} failed: {}", student.getStudentId(), ex.getMessage());
			return NotificationEmailStatus.FAILED;
		}
	}

	private StudentNotificationResponse toResponse(StudentNotification n) {
		StudentNotificationResponse r = new StudentNotificationResponse();
		r.setNotificationId(n.getId());
		r.setStudentId(n.getStudent().getStudentId());
		r.setInstructorId(n.getStaff().getStaffId());
		r.setInstructorName(fullName(n.getStaff().getFirstNm(), n.getStaff().getLastNm()));
		r.setBatchId(n.getClassBatch().getId());
		r.setBatchName(n.getClassBatch().getClassName());
		r.setCourseId(n.getCourse().getCourseId());
		r.setCourseTitle(n.getCourse().getCourseTitle());
		r.setMessage(n.getMessage());
		r.setChannels(Arrays.stream(n.getChannels().split(",")).filter(s -> !s.isBlank())
				.map(NotificationChannel::valueOf).toList());
		r.setEmailStatus(n.getEmailStatus());
		r.setSentAt(n.getSentAt());
		return r;
	}

	private static String fullName(String first, String last) {
		return ((first == null ? "" : first) + " " + (last == null ? "" : last)).trim();
	}
}
