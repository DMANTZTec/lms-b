package com.dmantz.lms.service.Impl;

import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.dto.response.StudentClassesAttendanceResponse;
import com.dmantz.lms.dto.response.WeeklyClassesAttendedResponse;
import com.dmantz.lms.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms.entity.AttendanceStatus;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.ClassStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.mapper.ClassBatchMapper;
import com.dmantz.lms.mapper.ClassScheduleMapper;
import com.dmantz.lms.mapper.StudentCourseMapper;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.impl.StudentDashboardServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StudentDashboardServiceImplTest {

    @Mock
    private ClassScheduleRepository classScheduleRepository;

    @Mock
    private ClassScheduleMapper classScheduleMapper;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ScheduleAttendanceRepository scheduleAttendanceRepository;

    @InjectMocks
    private StudentDashboardServiceImpl dashboardService;

    @BeforeMethod
    public void setup() {
        dashboardService = null;
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeeklyScheduleSuccess() {

        String studentId = "S000001";

        // Mock data
        List<ClassSchedule> schedules = List.of(new ClassSchedule());
        List<ClassScheduleResponse> dtoList = List.of(new ClassScheduleResponse());

        when(classScheduleRepository.findWeeklySchedule(
                eq(studentId),
                any(LocalDate.class),
                any(LocalDate.class),
                eq(ClassStatus.SCHEDULED)
        )).thenReturn(schedules);

        when(classScheduleMapper.toDtoList(schedules)).thenReturn(dtoList);

        // Call service
        WeeklyScheduleResponse response = dashboardService.getWeeklySchedule(studentId);

        // Assertions
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStudentId(), studentId);
        Assert.assertEquals(response.getTotalClasses(), 1);
        Assert.assertEquals(response.getClasses().size(), 1);
    }

    @Test
    public void testGetClassesAttendedPerWeekSuccess() {

        String studentId = "S000001";

        when(studentRepository.findByStudentId(studentId)).thenReturn(Optional.of(new Student()));

        when(scheduleAttendanceRepository.countByStudent_StudentIdAndStatusAndSchedule_ClassDateBetween(
                eq(studentId), eq(AttendanceStatus.PRESENT),
                any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(0L);

        List<WeeklyClassesAttendedResponse> response =
                dashboardService.getClassesAttendedPerWeek(studentId, 4);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.size(), 4);

        LocalDate currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        Assert.assertEquals(response.get(0).getWeekStart(), currentWeekStart.minusWeeks(3));
        Assert.assertEquals(response.get(0).getWeekEnd(), currentWeekStart.minusWeeks(3).plusDays(6));
        Assert.assertEquals(response.get(3).getWeekStart(), currentWeekStart);
        Assert.assertEquals(response.get(3).getWeekEnd(), currentWeekStart.plusDays(6));
        Assert.assertEquals(response.get(3).getClassesAttendedCount(), 0);
    }

    @Test
    public void testGetClassesAttendanceSummarySuccess() {

        String studentId = "S000001";

        when(studentRepository.findByStudentId(studentId)).thenReturn(Optional.of(new Student()));

        when(scheduleAttendanceRepository.countByStudent_StudentIdAndStatus(
                studentId, AttendanceStatus.PRESENT)).thenReturn(42L);
        when(scheduleAttendanceRepository.countByStudent_StudentIdAndStatus(
                studentId, AttendanceStatus.ABSENT)).thenReturn(8L);
        when(scheduleAttendanceRepository.countByStudent_StudentIdAndStatusAndSchedule_ClassDateBetween(
                eq(studentId), eq(AttendanceStatus.PRESENT),
                any(LocalDate.class), any(LocalDate.class))).thenReturn(10L);
        when(scheduleAttendanceRepository.countByStudent_StudentIdAndStatusAndSchedule_ClassDateBetween(
                eq(studentId), eq(AttendanceStatus.ABSENT),
                any(LocalDate.class), any(LocalDate.class))).thenReturn(3L);

        StudentClassesAttendanceResponse response =
                dashboardService.getClassesAttendanceSummary(studentId);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getClasses());
        Assert.assertEquals(response.getClasses().getAttended(), 42);
        Assert.assertEquals(response.getClasses().getSkipped(), 8);
        Assert.assertEquals(response.getClasses().getMonthAttended(), 10);
        Assert.assertEquals(response.getClasses().getMonthSkipped(), 3);
    }

}
