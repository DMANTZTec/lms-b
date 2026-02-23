package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.StudentMyCoursesResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms_b.entity.*;
import com.dmantz.lms_b.mapper.ClassScheduleMapper;
import com.dmantz.lms_b.mapper.StudentCourseMapper;
import com.dmantz.lms_b.repository.ClassBatchRepository;
import com.dmantz.lms_b.repository.ClassScheduleRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.repository.StudentCourseRepository;
import com.dmantz.lms_b.service.StudentDashboardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class StudentDashboardServiceImpl implements StudentDashboardService {


    private final ClassScheduleRepository classScheduleRepository;
    private final ClassScheduleMapper classScheduleMapper;
    private final ClassBatchRepository classBatchRepository;
    private final StaffRepository staffRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final StudentCourseMapper studentCourseMapper;

    public StudentDashboardServiceImpl(ClassScheduleRepository classScheduleRepository, ClassScheduleMapper classScheduleMapper, ClassBatchRepository classBatchRepository, StaffRepository staffRepository, StudentCourseRepository studentCourseRepository, StudentCourseMapper studentCourseMapper) {
        this.classScheduleRepository = classScheduleRepository;
        this.classScheduleMapper = classScheduleMapper;
        this.classBatchRepository = classBatchRepository;
        this.staffRepository = staffRepository;
        this.studentCourseRepository = studentCourseRepository;
        this.studentCourseMapper = studentCourseMapper;
    }


    @Override
    public WeeklyScheduleResponse getWeeklySchedule(String studentId) {

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);

        List<ClassSchedule> schedules =
                classScheduleRepository.findWeeklySchedule(
                        studentId,
                        weekStart,
                        weekEnd,
                        ClassStatus.SCHEDULED
                );

        List<ClassScheduleResponse> classDtos =
                classScheduleMapper.toDtoList(schedules);

        WeeklyScheduleResponse response = new WeeklyScheduleResponse();
        response.setStudentId(studentId);
        response.setWeekStart(weekStart);
        response.setWeekEnd(weekEnd);
        response.setTotalClasses((long) classDtos.size());
        response.setClasses(classDtos);

        return response;
    }


    @Override
    public StudentMyCoursesResponse getMyCourses(String studentId, CourseStatus status) {

        //  fetch all for counts
        List<StudentCourse> allCourses =
                studentCourseRepository.findByStudentStudentId(studentId);

        //  fetch filtered list for tab
        List<StudentCourse> filteredCourses =
                (status == null)
                        ? allCourses
                        : studentCourseRepository.findByStudentStudentIdAndStatus(studentId, status);

        StudentMyCoursesResponse response = new StudentMyCoursesResponse();

        // counts
        response.setTotalCourses(allCourses.size());
        response.setOngoing(countByStatus(allCourses, CourseStatus.ONGOING));
        response.setPlanned(countByStatus(allCourses, CourseStatus.PLANNED));
        response.setCompleted(countByStatus(allCourses, CourseStatus.COMPLETED));

        // course list
        response.setCourses(
                filteredCourses.stream()
                        .map(studentCourseMapper::toDto)
                        .toList()
        );

        return response;
    }

    private long countByStatus(List<StudentCourse> list, CourseStatus status) {
        return list.stream()
                .filter(c -> c.getStatus() == status)
                .count();
    }
}




//    @Override
//    public StudentDashboardResponse getDashboard(String studentId) {
//        List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);
//        return studentCourseMapper.toDashboard(studentCourses);
//    }

