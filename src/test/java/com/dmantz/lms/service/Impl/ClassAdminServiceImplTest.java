//package com.dmantz.lms.service.Impl;
//
//import com.dmantz.lms.dto.request.ClassScheduleRequest;
//import com.dmantz.lms.dto.request.CreateClassRequest;
//import com.dmantz.lms.dto.request.UpdateClassRequest;
//import com.dmantz.lms.dto.response.ClassResponse;
//import com.dmantz.lms.dto.response.ClassScheduleResponse;
//import com.dmantz.lms.entity.*;
//import com.dmantz.lms.mapper.ClassBatchMapper;
//import com.dmantz.lms.mapper.ClassScheduleMapper;
//import com.dmantz.lms.mapper.ClassTopicMapper;
//import com.dmantz.lms.mapper.StudentCourseMapper;
//import com.dmantz.lms.repository.*;
//import com.dmantz.lms.service.impl.ClassAdminServiceImpl;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.testng.AssertJUnit.assertEquals;
//import static org.testng.AssertJUnit.assertNotNull;
//
//public class ClassAdminServiceImplTest {
//
//    @Mock
//    private CourseRepository courseRepository;
//
//    @Mock
//    private ClassBatchRepository classBatchRepository;
//
//    @Mock
//    private ClassBatchMapper classBatchMapper;
//
//    private ClassAdminServiceImpl service;
//
//    @Mock
//    private ClassScheduleMapper classScheduleMapper;
//
//    @Mock
//    private StaffRepository staffRepository;
//
//    @Mock
//    private ClassScheduleRepository classScheduleRepository;
//
//    @Mock
//    private StudentRepository studentRepository;
//
//    @Mock
//    private StudentCourseRepository studentCourseRepository;
//
//    @Mock
//    private StudentCourseMapper studentCourseMapper;
//
//    @Mock
//    private ClassTopicRepository classTopicRepository;
//
//    @Mock
//    private TopicRepository topicRepository;
//
//    @Mock
//    private ClassTopicMapper classTopicMapper;
//
//    @BeforeMethod
//    public void setup() {
//
//        MockitoAnnotations.openMocks(this);
//
//        service = new ClassAdminServiceImpl(
//                courseRepository,
//                classBatchRepository,
//                classBatchMapper,
//                classScheduleMapper,
//                staffRepository,
//                classScheduleRepository,
//                studentRepository,
//                studentCourseRepository,
//                studentCourseMapper,
//                classTopicRepository,
//                topicRepository,
//                classTopicMapper
//        );
//    }
//
//    @Test
//    public void testAddClassSuccess() {
//
//        String courseId = "C001";
//
//        CreateClassRequest request = new CreateClassRequest();
//        request.setClassName("Java Batch");
//
//        Course course = new Course();
//        ClassBatch classBatch = new ClassBatch();
//        ClassResponse response = new ClassResponse();
//
//        when(courseRepository.findByCourseId(courseId)).thenReturn(Optional.of(course));
//        when(classBatchMapper.toEntity(request)).thenReturn(classBatch);
//        when(classBatchRepository.save(classBatch)).thenReturn(classBatch);
//        when(classBatchMapper.toResponse(classBatch)).thenReturn(response);
//
//        ClassResponse result = service.addClass(courseId, request);
//
//        assertNotNull(result);
//        verify(classBatchRepository).save(classBatch);
//    }
//
//
//    @Test(expectedExceptions = RuntimeException.class)
//    public void testModifyClassNotFound() {
//        when(classBatchRepository.findById(1L)).thenReturn(Optional.empty());
//        service.modifyClass(1L, new UpdateClassRequest());
//    }
//
//    @Test
//    public void testModifyClassSuccess() {
//
//        Long batchId = 1L;
//
//        UpdateClassRequest request = new UpdateClassRequest();
//
//        ClassBatch batch = new ClassBatch();
//        ClassResponse response = new ClassResponse();
//
//        reset(classBatchRepository, classBatchMapper);
//        when(classBatchRepository.findById(anyLong())).thenReturn(Optional.of(batch));
//
//        doNothing().when(classBatchMapper).updateClassFromDto(any(), any());
//        when(classBatchRepository.save(any())).thenReturn(batch);
//        when(classBatchMapper.toResponse(any())).thenReturn(response);
//
//        ClassResponse result = service.modifyClass(batchId, request);
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testCancelClassSuccess() {
//
//        Long batchId = 1L;
//
//        ClassBatch batch = new ClassBatch();
//        ClassResponse response = new ClassResponse();
//
//        when(classBatchRepository.findById(batchId)).thenReturn(Optional.of(batch));
//        when(classBatchRepository.save(batch)).thenReturn(batch);
//        when(classBatchMapper.toResponse(batch)).thenReturn(response);
//
//        ClassResponse result = service.cancelClass(batchId);
//
//        assertNotNull(result);
//        assertEquals("CANCELLED", batch.getStatus());
//    }
//
//    @Test(expectedExceptions = RuntimeException.class)
//    public void testCancelClassNotFound() {
//        when(classBatchRepository.findById(1L)).thenReturn(Optional.empty());
//        service.cancelClass(1L);
//    }
//
//    @Test
//    public void testAddScheduleSuccess() {
//
//        ClassScheduleRequest request = new ClassScheduleRequest();
//        request.setClassId(1L);
//        request.setStaffId(2L);
//
//        ClassSchedule schedule = new ClassSchedule();
//        ClassBatch batch = new ClassBatch();
//        Staff staff = new Staff();
//        ClassScheduleResponse response = new ClassScheduleResponse();
//
//        when(classScheduleMapper.toEntity(request)).thenReturn(schedule);
//        when(classBatchRepository.findById(1L)).thenReturn(Optional.of(batch));
//        when(staffRepository.findById(2L)).thenReturn(Optional.of(staff));
//        when(classScheduleRepository.save(schedule)).thenReturn(schedule);
//        when(classScheduleMapper.toResponse(schedule)).thenReturn(response);
//
//        ClassScheduleResponse result = service.addScheduleToClass(request);
//        assertNotNull(result);
//    }
//
//    @Test(expectedExceptions = RuntimeException.class)
//    public void testAddScheduleClassNotFound() {
//
//        ClassScheduleRequest request = new ClassScheduleRequest();
//        request.setClassId(1L);
//        request.setStaffId(2L);
//
//        when(classScheduleMapper.toEntity(request)).thenReturn(new ClassSchedule());
//        when(classBatchRepository.findById(1L)).thenReturn(Optional.empty());
//
//        service.addScheduleToClass(request);
//    }
//
//    @Test(expectedExceptions = RuntimeException.class)
//    public void testAddScheduleStaffNotFound() {
//
//        ClassScheduleRequest request = new ClassScheduleRequest();
//        request.setClassId(1L);
//        request.setStaffId(2L);
//
//        when(classScheduleMapper.toEntity(request)).thenReturn(new ClassSchedule());
//        when(classBatchRepository.findById(1L)).thenReturn(Optional.of(new ClassBatch()));
//        when(staffRepository.findById(2L)).thenReturn(Optional.empty());
//
//        service.addScheduleToClass(request);
//    }
//
//    @Test
//    public void testModifyScheduleSuccess() {
//
//        Long scheduleId = 1L;
//
//        ClassScheduleRequest request = new ClassScheduleRequest();
//        request.setStartTime(java.time.LocalTime.now());
//        request.setEndTime(java.time.LocalTime.now().plusHours(1));
//
//        ClassSchedule schedule = new ClassSchedule();
//        ClassScheduleResponse response = new ClassScheduleResponse();
//
//        when(classScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
//        when(classScheduleRepository.save(schedule)).thenReturn(schedule);
//        when(classScheduleMapper.toResponse(schedule)).thenReturn(response);
//
//        ClassScheduleResponse result = service.modifySchedule(scheduleId, request);
//        assertNotNull(result);
//    }
//
//    @Test(expectedExceptions = RuntimeException.class)
//    public void testModifyScheduleNotFound() {
//        when(classScheduleRepository.findById(1L)).thenReturn(Optional.empty());
//        service.modifySchedule(1L, new ClassScheduleRequest());
//    }
//
//    @Test
//    public void testCancelScheduleSuccess() {
//
//        ClassSchedule schedule = new ClassSchedule();
//        ClassScheduleResponse response = new ClassScheduleResponse();
//
//        when(classScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
//        when(classScheduleRepository.save(schedule)).thenReturn(schedule);
//        when(classScheduleMapper.toResponse(schedule)).thenReturn(response);
//
//        ClassScheduleResponse result = service.cancelSchedule(1L);
//
//        assertNotNull(result);
//        assertEquals(ClassStatus.CANCELLED, schedule.getStatus());
//    }
//}
