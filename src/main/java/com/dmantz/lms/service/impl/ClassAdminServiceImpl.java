package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.CourseAlreadyAssignedException;
import com.dmantz.lms.exceptions.CourseNotFoundException;
import com.dmantz.lms.exceptions.StudentNotFoundException;
import com.dmantz.lms.mapper.ClassBatchMapper;
import com.dmantz.lms.mapper.ClassScheduleMapper;
import com.dmantz.lms.mapper.ClassTopicMapper;
import com.dmantz.lms.mapper.StudentCourseMapper;
import com.dmantz.lms.repository.*;
import com.dmantz.lms.service.ClassAdminService;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClassAdminServiceImpl implements ClassAdminService {

    private final CourseRepository courseRepository;
    private final ClassBatchRepository classBatchRepository;
    private final ClassBatchMapper classBatchMapper;
    private final ClassScheduleMapper classScheduleMapper;
    private final StaffRepository staffRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final StudentCourseMapper studentCourseMapper;
    private final ClassTopicRepository classTopicRepository;
    private final TopicRepository topicRepository;
    private final ClassTopicMapper classTopicMapper;

    public ClassAdminServiceImpl(CourseRepository courseRepository, ClassBatchRepository classBatchRepository, ClassBatchMapper classBatchMapper, ClassScheduleMapper classScheduleMapper, StaffRepository staffRepository, ClassScheduleRepository classScheduleRepository, StudentRepository studentRepository, StudentCourseRepository studentCourseRepository, StudentCourseMapper studentCourseMapper, ClassTopicRepository classTopicRepository, TopicRepository topicRepository, ClassTopicMapper classTopicMapper) {
        this.courseRepository = courseRepository;
        this.classBatchRepository = classBatchRepository;
        this.classBatchMapper = classBatchMapper;
        this.classScheduleMapper = classScheduleMapper;
        this.staffRepository = staffRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.studentRepository = studentRepository;
        this.studentCourseRepository = studentCourseRepository;
        this.studentCourseMapper = studentCourseMapper;
        this.classTopicRepository = classTopicRepository;
        this.topicRepository = topicRepository;
        this.classTopicMapper = classTopicMapper;
    }


    @Override
    public ClassResponse addClass(String courseId, CreateClassRequest request) {

        // Fetch course using business ID
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Map request to entity
        ClassBatch classBatch = classBatchMapper.toEntity(request);

        // IMPORTANT: set relation
        classBatch.setCourse(course);

        if (classBatch.getStatus() == null) {
            classBatch.setStatus(String.valueOf(ClassStatus.SCHEDULED));
        }

        // Save
        classBatch = classBatchRepository.save(classBatch);

        return classBatchMapper.toResponse(classBatch);
    }

    @Override
    public ClassResponse modifyClass(Long batchId, UpdateClassRequest request) {

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Update only allowed fields
        classBatchMapper.updateClassFromDto(request, classBatch);

        classBatch = classBatchRepository.save(classBatch);

        return classBatchMapper.toResponse(classBatch);
    }

    @Override
    public ClassResponse cancelClass(Long batchId) {

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        classBatch.setStatus("CANCELLED");

        classBatch = classBatchRepository.save(classBatch);

        return classBatchMapper.toResponse(classBatch);
    }


    @Override
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
    public ClassScheduleResponse modifySchedule(Long scheduleId,
                                                ClassScheduleRequest request) {

        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // update fields
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        // update staff if changed
        if (request.getStaffId() != null) {
            Staff staff = staffRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            schedule.setStaff(staff);
        }

        // update class if changed
        if (request.getClassId() != null) {
            ClassBatch batch = classBatchRepository.findById(request.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            schedule.setClassBatch(batch);
        }

        ClassSchedule updated = classScheduleRepository.save(schedule);
        return classScheduleMapper.toResponse(updated);
    }

    @Override
    public ClassScheduleResponse cancelSchedule(Long scheduleId) {

        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setStatus(ClassStatus.CANCELLED);

        ClassSchedule updated = classScheduleRepository.save(schedule);
        return classScheduleMapper.toResponse(updated);
    }

	@Override
	public ClassAdminStudentDetailsResponse viewStudentDetails(String studentId) {
		
	    Student student = studentRepository.findByStudentId(studentId)
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    ClassAdminStudentDetailsResponse dto = new ClassAdminStudentDetailsResponse();
	    dto.setId(student.getId());
	    dto.setStudentId(student.getStudentId());
	    dto.setFirstNm(student.getFirstNm());
	    dto.setLastNm(student.getLastNm());
	    dto.setEmailId(student.getEmailId());
	    dto.setMobileNum(student.getMobileNum());
	    dto.setStatus(student.getStatus());
	    dto.setEnabled(student.getEnabled());

	   
	    List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);
	    List<MyCourseResponse> courseDtos = studentCourses.stream()
	            .map(studentCourseMapper::toDto)
	            .toList();
	    dto.setCourses(courseDtos);

	   
	    List<ClassSchedule> allSchedules = classScheduleRepository.findAllSchedulesForStudent(studentId);
	    List<ClassScheduleResponse> scheduleDtos = classScheduleMapper.toDtoList(allSchedules);

	    dto.setSchedules(scheduleDtos);
	    dto.setTotalSchedules(allSchedules.size());

	    LocalDate today = LocalDate.now();

	    long upcoming = allSchedules.stream()
	            .filter(s -> s.getStatus() == ClassStatus.SCHEDULED
	                    && !s.getClassDate().isBefore(today))
	            .count();

	    long completed = allSchedules.stream()
	            .filter(s -> s.getStatus() == ClassStatus.COMPLETED)
	            .count();

	    dto.setUpcoming(upcoming);
	    dto.setCompletedSchedules(completed);

	    return dto;

	}

	
	@Override
	public List<ClassAdminStudentDetailsResponse> viewStudents() {

	    List<Student> students = studentRepository.findAll();

	    return students.stream()
	            .map(s -> viewStudentDetails(s.getStudentId()))
	            .toList();
	}

    @Override
    public List<ClassScheduleResponse> getSchedulesByStaffId(String staffId) {

        List<ClassSchedule> schedules = classScheduleRepository.findByStaff_StaffId(staffId);

        return classScheduleMapper.toDtoList(schedules);
    }

    @Override
    public List<ClassScheduleResponse> getStaffDailySchedule(String staffId, LocalDate date) {

        List<ClassSchedule> schedules =
                classScheduleRepository.findByStaffStaffIdAndClassDate(staffId, date);

        return schedules.stream()
                .map(classScheduleMapper::toResponse)
                .toList();
    }

    @Override
    public void addTopicsToClass(Long batchId, AddClassTopicRequest request) {

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("ClassBatch not found"));

        for (AddClassTopicRequest.TopicItem item : request.getTopics()) {

            boolean exists = classTopicRepository
                    .existsByClassBatchIdAndTopicId(batchId, item.getTopicId());

            if (exists) {
                continue;
            }

            Topic topic = topicRepository.findById(item.getTopicId())
                    .orElseThrow(() -> new RuntimeException("Topic not found: " + item.getTopicId()));

            ClassTopic classTopic = new ClassTopic();
            classTopic.setClassBatch(classBatch);
            classTopic.setTopic(topic);
            classTopic.setStatus(item.getStatus());

            classTopicRepository.save(classTopic);
        }
    }

    @Override
    public void removeTopicsFromClass(Long batchId, RemoveClassTopicRequest request) {

        classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("ClassBatch not found"));

        classTopicRepository.deleteByClassBatchIdAndTopicIdIn(batchId,
                request.getTopicIds());
    }

    @Override
    public List<ClassTopicResponse> getTopicsByBatchId(Long batchId) {

        classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("ClassBatch not found"));

        List<ClassTopic> classTopics =
                classTopicRepository.findByClassBatchId(batchId);

        return classTopicMapper.toResponseList(classTopics);
    }

    @Override
    public StudentCourseResponse assignCourseToStudent(String studentId, String courseId) {

        Student student = studentRepository
                .findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + studentId));

        Course course = courseRepository
                .findByCourseId(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));

        // Check if already assigned
        boolean exists = studentCourseRepository
                .existsByStudent_StudentIdAndCourse_CourseId(
                        studentId,
                        courseId);

        if (exists) {
            throw new CourseAlreadyAssignedException("Course already assigned to student: " + courseId);
        }

        // Create enrollment
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourse.setStatus(CourseStatus.PLANNED);

        var saved = studentCourseRepository.save(studentCourse);

        return studentCourseMapper.toResponse(saved);
    }

}


