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

    
 //  CREATE SUBJECT

    @Test
    public void testCreateSubjectSuccess() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject subject = new Subject();
        Subject savedSubject = new Subject();
        SubjectResponse response = new SubjectResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findBySubjectShortCd("JV")).thenReturn(Optional.empty());
        when(subjectMapper.toEntity(request)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(savedSubject);
        when(subjectMapper.toDto(savedSubject)).thenReturn(response);

        SubjectResponse result = courseManagementService.createSubject(request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response); 
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testCreateSubjectStaffNotFound() {

        SubjectRequest request = new SubjectRequest();

        when(staffRepository.existsById(1L)).thenReturn(false);

        courseManagementService.createSubject(request, 1L);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testCreateSubjectDuplicateShortCode() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.of(new Subject()));

        courseManagementService.createSubject(request, 1L);
    }

    // VIEW ALL SUBJECTS 
    @Test
    public void testViewAllSubjectsSuccess() {

        Subject subject = new Subject();
        SubjectResponse response = new SubjectResponse();

        when(subjectRepository.findAll()).thenReturn(List.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(response);

        List<SubjectResponse> result = courseManagementService.viewAllSubjects();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), response);
    }

    @Test
    public void testViewAllSubjectsEmptyList() {

        when(subjectRepository.findAll()).thenReturn(List.of());

        List<SubjectResponse> result = courseManagementService.viewAllSubjects();

        Assert.assertEquals(result.size(), 0);
    }

    //  UPDATE SUBJECT 
    
    @Test
    public void testUpdateSubjectSuccessNoDuplicate() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject subject = new Subject();
        subject.setId(1L);

        SubjectResponse response = new SubjectResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectRepository.findBySubjectShortCd("JV")).thenReturn(Optional.empty()); 
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(response);

        SubjectResponse result = courseManagementService.updateSubject(1L, request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);
        verify(subjectMapper, times(1)).updateSubjectFromRequest(request, subject);
        verify(subjectRepository, times(1)).save(subject); // ✅ also verify save was called
    }

   
    @Test
    public void testUpdateSubjectSuccessSameShortCode() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject subject = new Subject();
        subject.setId(1L); 

        SubjectResponse response = new SubjectResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.of(subject)); 
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(response);

        SubjectResponse result = courseManagementService.updateSubject(1L, request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);
        verify(subjectMapper, times(1)).updateSubjectFromRequest(request, subject);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateSubjectStaffNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(false);

        courseManagementService.updateSubject(1L, new SubjectRequest(), 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateSubjectNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.updateSubject(1L, new SubjectRequest(), 1L);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testUpdateSubjectDuplicateShortCode() {

        SubjectRequest request = new SubjectRequest();
        request.setSubjectShortCd("JV");

        Subject existingSubject = new Subject();
        existingSubject.setId(1L);

        Subject anotherSubject = new Subject();
        anotherSubject.setId(99L); 

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject));
        when(subjectRepository.findBySubjectShortCd("JV"))
                .thenReturn(Optional.of(anotherSubject));

        courseManagementService.updateSubject(1L, request, 1L);
    }

    //  DELETE SUBJECT 

    @Test
    public void testDeleteSubjectSuccess() {

        Subject subject = new Subject();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        courseManagementService.deleteSubject(1L, 1L);

        verify(subjectRepository, times(1)).delete(subject);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testDeleteSubjectStaffNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(false);

        courseManagementService.deleteSubject(1L, 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testDeleteSubjectNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.deleteSubject(1L, 1L);
    }
    
    
    
 // CREATE COURSE 

    @Test
    public void testCreateCourseSuccess() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Basics");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubjectShortCd("JV");

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        Course savedCourse = new Course();
        CourseResponse response = new CourseResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Basics", 1L, 1L, "English")).thenReturn(false);
        when(courseRepository.findTopBySubject_SubjectShortCdOrderByIdDesc("JV"))
                .thenReturn(Optional.empty());
        when(courseRepository.existsByCourseId(any(String.class))).thenReturn(false);
        when(courseMapper.toEntity(request)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toDto(savedCourse)).thenReturn(response);

        CourseResponse result = courseManagementService.createCourse(request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);                              
        Assert.assertNotNull(course.getCourseId());                       
        Assert.assertTrue(course.getCourseId().startsWith("JV"));          
        verify(courseRepository, times(1)).save(course);
    }

    
    @Test
    public void testCreateCourseIdCollisionRetry() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Basics");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubjectShortCd("JV");

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        Course savedCourse = new Course();
        CourseResponse response = new CourseResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Basics", 1L, 1L, "English")).thenReturn(false);
        when(courseRepository.findTopBySubject_SubjectShortCdOrderByIdDesc("JV"))
                .thenReturn(Optional.empty());
        when(courseRepository.existsByCourseId(any(String.class)))
                .thenReturn(true)  
                .thenReturn(false); 
        when(courseMapper.toEntity(request)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toDto(savedCourse)).thenReturn(response);

        CourseResponse result = courseManagementService.createCourse(request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);
        Assert.assertEquals(course.getCourseId(), "JV002");  
        verify(courseRepository, times(1)).save(course);
    }

    
    @Test
    public void testCreateCourseIdIncrementFromLastCourse() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Advanced");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSubjectShortCd("JV");

        Provider provider = new Provider();
        provider.setId(1L);

        Course lastCourse = new Course();
        lastCourse.setCourseId("JV003"); 

        Course course = new Course();
        Course savedCourse = new Course();
        CourseResponse response = new CourseResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Advanced", 1L, 1L, "English")).thenReturn(false);
        when(courseRepository.findTopBySubject_SubjectShortCdOrderByIdDesc("JV"))
                .thenReturn(Optional.of(lastCourse)); 
        when(courseRepository.existsByCourseId(any(String.class))).thenReturn(false);
        when(courseMapper.toEntity(request)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toDto(savedCourse)).thenReturn(response);

        CourseResponse result = courseManagementService.createCourse(request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(course.getCourseId(), "JV004"); 
        verify(courseRepository, times(1)).save(course);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testCreateCourseStaffNotFound() {

        CourseRequest request = new CourseRequest();

        when(staffRepository.existsById(1L)).thenReturn(false);

        courseManagementService.createCourse(request, 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testCreateCourseSubjectNotFound() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.createCourse(request, 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testCreateCourseProviderNotFound() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);

        Subject subject = new Subject();
        subject.setId(1L);

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.createCourse(request, 1L);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testCreateCourseDuplicate() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Basics");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);

        Provider provider = new Provider();
        provider.setId(1L);

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Basics", 1L, 1L, "English")).thenReturn(true);

        courseManagementService.createCourse(request, 1L);
    }

    //  VIEW ALL COURSES 

    @Test
    public void testViewAllCoursesSuccess() {

        Course course = new Course();
        CourseResponse response = new CourseResponse();

        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(courseMapper.toDto(course)).thenReturn(response);

        List<CourseResponse> result = courseManagementService.viewAllCourses();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), response);
    }

    @Test
    public void testViewAllCoursesEmptyList() {

        when(courseRepository.findAll()).thenReturn(List.of());

        List<CourseResponse> result = courseManagementService.viewAllCourses();

        Assert.assertEquals(result.size(), 0);
    }

    //  VIEW COURSES BY SUBJECT 
    @Test
    public void testViewCoursesBySubjectSuccess() {

        Course course = new Course();
        CourseResponse response = new CourseResponse();

        when(subjectRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findBySubject_Id(1L)).thenReturn(List.of(course));
        when(courseMapper.toDto(course)).thenReturn(response);

        List<CourseResponse> result = courseManagementService.viewCoursesBySubject(1L);

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), response);
    }

    @Test
    public void testViewCoursesBySubjectEmptyList() {

        when(subjectRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findBySubject_Id(1L)).thenReturn(List.of());

        List<CourseResponse> result = courseManagementService.viewCoursesBySubject(1L);

        Assert.assertEquals(result.size(), 0);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testViewCoursesBySubjectNotFound() {

        when(subjectRepository.existsById(1L)).thenReturn(false);

        courseManagementService.viewCoursesBySubject(1L);
    }

    //  UPDATE COURSE 

    @Test
    public void testUpdateCourseSuccess() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Basics");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        course.setCourseTitle("Java Basics");
        course.setLanguage("English");
        course.setSubject(subject);
        course.setProvider(provider);

        CourseResponse response = new CourseResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Basics", 1L, 1L, "English")).thenReturn(false); 
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(response);

        CourseResponse result = courseManagementService.updateCourse(1L, request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);                                          
        verify(courseMapper, times(1)).updateCourseFromRequest(request, course);
        verify(courseRepository, times(1)).save(course);                              
    }

   
    @Test
    public void testUpdateCourseSameDataAllowed() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Basics");
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        course.setCourseTitle("Java Basics");   
        course.setLanguage("English");          
        course.setSubject(subject);              
        course.setProvider(provider);            

        CourseResponse response = new CourseResponse();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Basics", 1L, 1L, "English")).thenReturn(true); 
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(response);

        CourseResponse result = courseManagementService.updateCourse(1L, request, 1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, response);
        verify(courseMapper, times(1)).updateCourseFromRequest(request, course);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateCourseStaffNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(false);

        courseManagementService.updateCourse(1L, new CourseRequest(), 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateCourseNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.updateCourse(1L, new CourseRequest(), 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateCourseSubjectNotFound() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);

        Course course = new Course();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.updateCourse(1L, request, 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateCourseProviderNotFound() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);

        Subject subject = new Subject();
        subject.setId(1L);

        Course course = new Course();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.updateCourse(1L, request, 1L);
    }

    @Test(expectedExceptions = DuplicateValuesException.class)
    public void testUpdateCourseDuplicate() {

        CourseRequest request = new CourseRequest();
        request.setSubjectId(1L);
        request.setProviderId(1L);
        request.setCourseTitle("Java Advanced"); 
        request.setLanguage("English");

        Subject subject = new Subject();
        subject.setId(1L);

        Provider provider = new Provider();
        provider.setId(1L);

        Course course = new Course();
        course.setCourseTitle("Java Basics");    
        course.setLanguage("English");
        course.setSubject(subject);
        course.setProvider(provider);

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
                "Java Advanced", 1L, 1L, "English")).thenReturn(true); 

        courseManagementService.updateCourse(1L, request, 1L);
    }

    //  DELETE COURSE 

    @Test
    public void testDeleteCourseSuccess() {

        Course course = new Course();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseManagementService.deleteCourse(1L, 1L);

        verify(courseRepository, times(1)).delete(course);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testDeleteCourseStaffNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(false);

        courseManagementService.deleteCourse(1L, 1L);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testDeleteCourseNotFound() {

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        courseManagementService.deleteCourse(1L, 1L);
    }
    
    

}


    

