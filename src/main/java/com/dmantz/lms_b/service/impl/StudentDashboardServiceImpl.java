package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms_b.entity.*;
import com.dmantz.lms_b.mapper.ClassScheduleMapper;
import com.dmantz.lms_b.repository.ClassBatchRepository;
import com.dmantz.lms_b.repository.ClassScheduleRepository;
import com.dmantz.lms_b.repository.StaffRepository;
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

    public StudentDashboardServiceImpl(ClassScheduleRepository classScheduleRepository, ClassScheduleMapper classScheduleMapper, ClassBatchRepository classBatchRepository, StaffRepository staffRepository) {
        this.classScheduleRepository = classScheduleRepository;
        this.classScheduleMapper = classScheduleMapper;
        this.classBatchRepository = classBatchRepository;
        this.staffRepository = staffRepository;
    }


    @Override
    @Transactional
    public ClassScheduleResponse addScheduleToClass(ClassScheduleRequest request) {

        ClassSchedule schedule = classScheduleMapper.toEntity(request);

        if (schedule.getStatus() == null) {
            schedule.setStatus(ClassStatus.SCHEDULED);
        }

        ClassBatch batch = classBatchRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        schedule.setClassBatch(batch);
        schedule.setStaff(staff);

        ClassSchedule saved = classScheduleRepository.save(schedule);
        return classScheduleMapper.toResponse(saved);
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




//    @Override
//    public StudentDashboardResponse getDashboard(String studentId) {
//        List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);
//        return studentCourseMapper.toDashboard(studentCourses);
//    }
}
