package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.StudentScheduleResponse;
import com.dmantz.lms_b.entity.ClassSchedule;
import com.dmantz.lms_b.entity.ClassStatus;
import com.dmantz.lms_b.entity.StudentCourse;
import com.dmantz.lms_b.mapper.StudentCourseMapper;
import com.dmantz.lms_b.mapper.StudentScheduleMapper;
import com.dmantz.lms_b.repository.ClassScheduleRepository;
import com.dmantz.lms_b.repository.StudentCourseRepository;
import com.dmantz.lms_b.service.StudentDashboardService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentDashboardServiceImpl implements StudentDashboardService {

    private final StudentCourseRepository studentCourseRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final StudentScheduleMapper mapper;
    private final StudentCourseMapper studentCourseMapper;

    public StudentDashboardServiceImpl(StudentCourseRepository studentCourseRepository, ClassScheduleRepository classScheduleRepository, StudentScheduleMapper mapper, StudentCourseMapper studentCourseMapper) {
        this.studentCourseRepository = studentCourseRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.mapper = mapper;
        this.studentCourseMapper = studentCourseMapper;
    }

    @Override
    public List<StudentScheduleResponse> getMyClassScheduleThisWeek(String studentId) {

        List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);

        List<Long> courseIds = studentCourses.stream()
                .map(sc -> sc.getCourse().getId())
                .toList();

        if (courseIds.isEmpty()) {
            return List.of();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        List<ClassSchedule> schedules = classScheduleRepository
                        .findByCourseIdInAndClassDateBetweenAndStatusNotOrderByClassDateAscStartTimeAsc(
                                courseIds,
                                startOfWeek,
                                endOfWeek,
                                ClassStatus.CANCELLED
                        );

        return mapper.toDtoList(schedules);
    }

    @Override
    public StudentDashboardResponse getDashboard(String studentId) {
        List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);
        return studentCourseMapper.toDashboard(studentCourses);
    }
}
