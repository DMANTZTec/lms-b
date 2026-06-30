package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms.dto.response.StudentCourseResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.CourseStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentCourse;
import com.dmantz.lms.exceptions.DuplicateEnrollmentException;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentCourseMapper;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.StudentCourseRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.StudentCourseService;

import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StudentCourseServiceImpl implements StudentCourseService {

	private static final Logger logger = LogManager.getLogger(StudentCourseServiceImpl.class);

	private final StudentCourseRepository studentCourseRepository;
	private final StudentCourseMapper studentCourseMapper;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	public StudentCourseServiceImpl(StudentCourseRepository studentCourseRepository,
			StudentCourseMapper studentCourseMapper, StudentRepository studentRepository,
			CourseRepository courseRepository) {

		this.studentCourseRepository = studentCourseRepository;
		this.studentCourseMapper = studentCourseMapper;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	// ================= ENROLL COURSE =================

	@Override
	public StudentCourseResponse enroll(StudentCourseEnrollRequest request) {

		logger.info("Student course enrollment started for studentId: {} and courseId: {}", request.getStudentId(),
				request.getCourseId());

		Student student = studentRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> {

			logger.error("Student not found with studentId: {}", request.getStudentId());

			return new ResourceNotFoundException("Student not found with studentId: " + request.getStudentId());
		});

		Course course = courseRepository.findByCourseId(request.getCourseId()).orElseThrow(() -> {

			logger.error("Course not found with courseId: {}", request.getCourseId());

			return new ResourceNotFoundException("Course not found with courseId: " + request.getCourseId());
		});

		studentCourseRepository.findByStudent_IdAndCourse_Id(student.getId(), course.getId()).ifPresent(sc -> {

			logger.error("Student already enrolled in course. studentId: {}, courseId: {}", request.getStudentId(),
					request.getCourseId());

			throw new DuplicateEnrollmentException("Student already enrolled in this course");
		});

		StudentCourse entity = new StudentCourse();

		entity.setStudent(student);
		entity.setCourse(course);
		entity.setStatus(CourseStatus.PLANNED);
		entity.setStart_dt(LocalDateTime.now());
		entity.setEnrolledDt(LocalDateTime.now());

		StudentCourse saved = studentCourseRepository.save(entity);

		logger.info("Student enrolled successfully for studentId: {} and courseId: {}", request.getStudentId(),
				request.getCourseId());

		return studentCourseMapper.toResponse(saved);
	}

	// ================= GET STUDENT COURSES =================

	@Override
	public List<StudentCourseResponse> getStudentCourses(String studentId) {

		logger.info("Fetching enrolled courses for studentId: {}", studentId);

		List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);

		if (studentCourses.isEmpty()) {

			logger.warn("No enrolled courses found for studentId: {}", studentId);

			throw new ResourceNotFoundException("No enrolled courses found for studentId: " + studentId);
		}

		logger.info("Successfully fetched enrolled courses for studentId: {}", studentId);

		return studentCourses.stream().map(studentCourseMapper::toResponse).toList();
	}
}