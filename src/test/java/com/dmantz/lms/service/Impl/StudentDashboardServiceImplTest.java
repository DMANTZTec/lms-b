package com.dmantz.lms.service.Impl;

import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.dto.response.WeeklyScheduleResponse;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.ClassStatus;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StudentDashboardServiceImplTest {

    @Mock
    private ClassScheduleRepository classScheduleRepository;

    @Mock
    private ClassScheduleMapper classScheduleMapper;

    @InjectMocks
    private StudentDashboardServiceImpl dashboardService;

    @BeforeMethod
    public void setup() {
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

}
