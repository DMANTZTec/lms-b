package com.dmantz.lms.service.Impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dmantz.lms.dto.request.CourseRequest;
import com.dmantz.lms.dto.request.SubjectRequest;
import com.dmantz.lms.dto.response.CourseResponse;
import com.dmantz.lms.dto.response.SubjectResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.Provider;
import com.dmantz.lms.entity.Subject;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.*;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.impl.CourseManagementServiceImpl;

public class CourseManagementServiceImplTest {

    @Mock private SubjectRepository subjectRepository;
    @Mock private StaffRepository staffRepository;
    @Mock private SubjectMapper subjectMapper;

    @Mock private CourseRepository courseRepository;
    @Mock private CourseMapper courseMapper;
    @Mock private ProviderRepository providerRepository;

    @Mock private ChapterRepository chapterRepository;
    @Mock private ChapterMapper chapterMapper;

    @Mock private TopicRepository topicRepository;
    @Mock private TopicMapper topicMapper;

    @Mock private TopicReferenceRepository topicReferenceRepository;
    @Mock private TopicReferenceMapper topicReferenceMapper;

    @Mock private ProgramRepository programRepository;
    @Mock private ProgramCourseRepository programCourseRepository;
    @Mock private ProgramCourseMapper programcourseMapper;
    @Mock private ProgramMapper programMapper;

    private CourseManagementServiceImpl courseManagementService;

    private final String STAFF_ID = "STAFF001";

    @BeforeMethod
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        courseManagementService = new CourseManagementServiceImpl(
                subjectRepository,
                staffRepository,
                subjectMapper,
                courseRepository,
                courseMapper,
                providerRepository,
                chapterRepository,
                chapterMapper,
                topicRepository,
                topicMapper,
                topicReferenceRepository,
                topicReferenceMapper,
                programRepository,
                programCourseRepository,
                programcourseMapper,
                programMapper
        );
    }

    // ================= CREATE SUBJECT =================

    @Test
    public void testCreateSubjectSuccess() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject subject = new Subject();
        Subject savedSubject = new Subject();
        SubjectResponse response = new SubjectResponse();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);
        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.empty());

        when(subjectMapper.toEntity(request)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(savedSubject);
        when(subjectMapper.toDto(savedSubject)).thenReturn(response);

        SubjectResponse result =
                courseManagementService.createSubject(request, STAFF_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);

        verify(subjectRepository, times(1)).save(subject);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testCreateSubjectStaffNotFound() {

        SubjectRequest request = new SubjectRequest();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(false);

        courseManagementService.createSubject(request, STAFF_ID);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testCreateSubjectDuplicateShortCode() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.of(new Subject()));

        courseManagementService.createSubject(request, STAFF_ID);
    }

    // ================= VIEW ALL SUBJECTS =================

    @Test
    public void testViewAllSubjectsSuccess() {

        Subject subject = new Subject();
        SubjectResponse response = new SubjectResponse();

        when(subjectRepository.findAll()).thenReturn(List.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(response);

        List<SubjectResponse> result =
                courseManagementService.viewAllSubjects();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), response);
    }

    // ================= UPDATE SUBJECT =================

    @Test
    public void testUpdateSubjectSuccess() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject subject = new Subject();
        subject.setId(1L);

        SubjectResponse response = new SubjectResponse();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.empty());

        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(response);

        SubjectResponse result =
                courseManagementService.updateSubject(1L, request, STAFF_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);

        verify(subjectMapper, times(1))
                .updateSubjectFromRequest(request, subject);

        verify(subjectRepository, times(1)).save(subject);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testUpdateSubjectDuplicate() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject existing = new Subject();
        existing.setId(1L);

        Subject duplicate = new Subject();
        duplicate.setId(2L);

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.of(duplicate));

        courseManagementService.updateSubject(1L, request, STAFF_ID);
    }

    // ================= DELETE SUBJECT =================

    @Test
    public void testDeleteSubjectSuccess() {

        Subject subject = new Subject();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        courseManagementService.deleteSubject(1L, STAFF_ID);

        verify(subjectRepository, times(1)).delete(subject);
    }

    // ================= CREATE COURSE =================

    @Test
    public void testCreateCourseSuccess() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubjectShortCd("JV");

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        Course savedCourse = new Course();

        CourseResponse response = new CourseResponse();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        when(providerRepository.findById(1L))
                .thenReturn(Optional.of(provider));

        when(courseRepository
                .existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                        "Java",
                        1L,
                        1L,
                        "English"))
                .thenReturn(false);

        when(courseRepository
                .findTopBySubject_SubjectShortCdOrderByIdDesc("JV"))
                .thenReturn(Optional.empty());

        when(courseRepository.existsByCourseId(any(String.class)))
                .thenReturn(false);

        when(courseMapper.toEntity(request)).thenReturn(course);

        when(courseRepository.save(course)).thenReturn(savedCourse);

        when(courseMapper.toDto(savedCourse)).thenReturn(response);

        CourseResponse result =
                courseManagementService.createCourse(request, STAFF_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);

        verify(courseRepository, times(1)).save(course);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testCreateCourseDuplicate() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);

        Provider provider = new Provider();
        provider.setId(1L);

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        when(providerRepository.findById(1L))
                .thenReturn(Optional.of(provider));

        when(courseRepository
                .existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                        "Java",
                        1L,
                        1L,
                        "English"))
                .thenReturn(true);

        courseManagementService.createCourse(request, STAFF_ID);
    }

    // ================= VIEW ALL COURSES =================

    @Test
    public void testViewAllCoursesSuccess() {

        Course course = new Course();
        CourseResponse response = new CourseResponse();

        when(courseRepository.findAll()).thenReturn(List.of(course));

        when(courseMapper.toDto(course)).thenReturn(response);

        List<CourseResponse> result =
                courseManagementService.viewAllCourses();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), response);
    }

    // ================= UPDATE COURSE =================

    @Test
    public void testUpdateCourseSuccess() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        course.setSubject(subject);
        course.setProvider(provider);
        course.setCourseTitle("Old");
        course.setLanguage("English");

        CourseResponse response = new CourseResponse();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        when(providerRepository.findById(1L))
                .thenReturn(Optional.of(provider));

        when(courseRepository
                .existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                        "Java",
                        1L,
                        1L,
                        "English"))
                .thenReturn(false);

        when(courseRepository.save(course)).thenReturn(course);

        when(courseMapper.toDto(course)).thenReturn(response);

        CourseResponse result =
                courseManagementService.updateCourse(1L, request, STAFF_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);

        verify(courseRepository, times(1)).save(course);
    }

    // ================= DELETE COURSE =================

    @Test
    public void testDeleteCourseSuccess() {

        Course course = new Course();

        when(staffRepository.existsByStaffId(STAFF_ID)).thenReturn(true);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        courseManagementService.deleteCourse(1L, STAFF_ID);

        verify(courseRepository, times(1)).delete(course);
    }

    // ================= VIEW COURSES BY SUBJECT =================

    @Test
    public void testViewCoursesBySubjectSuccess() {

        Course course = new Course();
        CourseResponse response = new CourseResponse();

        when(subjectRepository.existsById(1L)).thenReturn(true);

        when(courseRepository.findBySubject_Id(1L))
                .thenReturn(List.of(course));

        when(courseMapper.toDto(course)).thenReturn(response);

        List<CourseResponse> result =
                courseManagementService.viewCoursesBySubject(1L);

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), response);
    }
}
