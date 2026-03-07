package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms_b.dto.response.StudentCourseResponse;
import com.dmantz.lms_b.entity.Course;
import com.dmantz.lms_b.entity.CourseStatus;
import com.dmantz.lms_b.entity.Student;
import com.dmantz.lms_b.entity.StudentCourse;
import com.dmantz.lms_b.mapper.StudentCourseMapper;
import com.dmantz.lms_b.repository.CourseRepository;
import com.dmantz.lms_b.repository.StudentCourseRepository;
import com.dmantz.lms_b.repository.StudentRepository;
import com.dmantz.lms_b.service.StudentCourseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StudentCourseServiceImpl implements StudentCourseService {

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

	@Override
	public StudentCourseResponse enroll(StudentCourseEnrollRequest request) {

		Student student = studentRepository.findByStudentId(request.getStudentId())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		Course course = courseRepository.findByCourseId(request.getCourseId())
				.orElseThrow(() -> new RuntimeException("Course not found"));

		studentCourseRepository.findByStudent_IdAndCourse_Id(student.getId(), course.getId()).ifPresent(sc -> {
			throw new RuntimeException("Student already enrolled in this course");
		});

		StudentCourse entity = new StudentCourse();
		entity.setStudent(student);
		entity.setCourse(course);
		entity.setStatus(CourseStatus.PLANNED);
		entity.setStart_dt(LocalDateTime.now());
		entity.setEnrolledDt(LocalDateTime.now());

		StudentCourse saved = studentCourseRepository.save(entity);
		return studentCourseMapper.toResponse(saved);
	}

	@Override
	public List<StudentCourseResponse> getStudentCourses(String studentId) {

		List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);

		return studentCourses.stream().map(studentCourseMapper::toResponse).toList();
	}

}
