package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.CourseAlreadyAssignedException;
import com.dmantz.lms.exceptions.CourseNotFoundException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClassAdminServiceImpl implements ClassAdminService {

	
	  private static final Logger logger = LogManager.getLogger(ClassAdminServiceImpl.class);

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
    	  logger.info("Creating class for courseId: {}", courseId);

          Course course = courseRepository.findByCourseId(courseId)
                  .orElseThrow(() -> {
                      logger.warn("Course not found with courseId: {} during addClass", courseId);
                      return new CourseNotFoundException("Course not found with ID: " + courseId);
                  });

        // Map request to entity
        ClassBatch classBatch = classBatchMapper.toEntity(request);

        // IMPORTANT: set relation
        classBatch.setCourse(course);

        if (classBatch.getStatus() == null) {
            classBatch.setStatus(String.valueOf(ClassStatus.SCHEDULED));
        }

        // Save
        classBatch = classBatchRepository.save(classBatch);

        logger.info("Class created successfully with batchId: {} for courseId: {}", classBatch.getId(), courseId);
        return classBatchMapper.toResponse(classBatch);
    }

    @Override
    public ClassResponse modifyClass(Long batchId, UpdateClassRequest request) {

    	   logger.info("Modifying class with batchId: {}", batchId);

           ClassBatch classBatch = classBatchRepository.findById(batchId)
                   .orElseThrow(() -> {
                       logger.warn("ClassBatch not found with id: {} during modifyClass", batchId);
                       return new ResourceNotFoundException("Class not found with id: " + batchId);
                   });
        // Update only allowed fields
        classBatchMapper.updateClassFromDto(request, classBatch);

        classBatch = classBatchRepository.save(classBatch);

        logger.info("Class modified successfully with batchId: {}", batchId);
        return classBatchMapper.toResponse(classBatch);
    }

    @Override
    public ClassResponse cancelClass(Long batchId) {


        logger.info("Cancelling class with batchId: {}", batchId);

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> {
                    logger.warn("ClassBatch not found with id: {} during cancelClass", batchId);
                    return new ResourceNotFoundException("Class not found with id: " + batchId);
                });
        
        classBatch.setStatus("CANCELLED");

        classBatch = classBatchRepository.save(classBatch);

        logger.info("Class cancelled successfully with batchId: {}", batchId);
        return classBatchMapper.toResponse(classBatch);
    }


    @Override
    public ClassScheduleResponse addScheduleToClass(ClassScheduleRequest request) {

    	logger.info("Adding schedule to classId: {} with staffId: {}", request.getClassId(), request.getStaffId());

    	
        ClassSchedule schedule = classScheduleMapper.toEntity(request);

        if (schedule.getStatus() == null) {
            schedule.setStatus(ClassStatus.SCHEDULED);
        }

        ClassBatch batch = classBatchRepository.findById(request.getClassId())
                .orElseThrow(() -> {
                    logger.warn("ClassBatch not found with id: {} during addScheduleToClass", request.getClassId());
                    return new ResourceNotFoundException("Class not found with id: " + request.getClassId());
                });

        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> {
                    logger.warn("Staff not found with id: {} during addScheduleToClass", request.getStaffId());
                    return new ResourceNotFoundException("Staff not found with id: " + request.getStaffId());
                });

        schedule.setClassBatch(batch);
        schedule.setStaff(staff);

        ClassSchedule saved = classScheduleRepository.save(schedule);
        
        logger.info("Schedule added successfully with id: {} to classId: {}", saved.getId(), request.getClassId());
        return classScheduleMapper.toResponse(saved);
    }

    @Override
    public ClassScheduleResponse modifySchedule(Long scheduleId,
                                                ClassScheduleRequest request) {

    	   logger.info("Modifying schedule with id: {}", scheduleId);

           ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                   .orElseThrow(() -> {
                       logger.warn("Schedule not found with id: {} during modifySchedule", scheduleId);
                       return new ResourceNotFoundException("Schedule not found with id: " + scheduleId);
                   });
           
        // update fields
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        // update staff if changed
        if (request.getStaffId() != null) {
            logger.debug("Updating staff to id: {} for scheduleId: {}", request.getStaffId(), scheduleId);
            Staff staff = staffRepository.findById(request.getStaffId())
                    .orElseThrow(() -> {
                        logger.warn("Staff not found with id: {} during modifySchedule", request.getStaffId());
                        return new ResourceNotFoundException("Staff not found with id: " + request.getStaffId());
                    });
            schedule.setStaff(staff);
        }

        // update class if changed
        if (request.getClassId() != null) {
            logger.debug("Updating class to id: {} for scheduleId: {}", request.getClassId(), scheduleId);
            ClassBatch batch = classBatchRepository.findById(request.getClassId())
                    .orElseThrow(() -> {
                        logger.warn("ClassBatch not found with id: {} during modifySchedule", request.getClassId());
                        return new ResourceNotFoundException("Class not found with id: " + request.getClassId());
                    });
            schedule.setClassBatch(batch);
        }

        ClassSchedule updated = classScheduleRepository.save(schedule);
        
        logger.info("Schedule modified successfully with id: {}", scheduleId);
        return classScheduleMapper.toResponse(updated);
    }

    @Override
    public ClassScheduleResponse cancelSchedule(Long scheduleId) {

    	logger.info("Cancelling schedule with id: {}", scheduleId);

        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> {
                    logger.warn("Schedule not found with id: {} during cancelSchedule", scheduleId);
                    return new ResourceNotFoundException("Schedule not found with id: " + scheduleId);
                });

        schedule.setStatus(ClassStatus.CANCELLED);

        ClassSchedule updated = classScheduleRepository.save(schedule);
        
        logger.info("Schedule cancelled successfully with id: {}", scheduleId);
        return classScheduleMapper.toResponse(updated);
    }

	@Override
	public ClassAdminStudentDetailsResponse viewStudentDetails(String studentId) {
		

        logger.info("Fetching student details for studentId: {}", studentId);

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.warn("Student not found with id: {} during viewStudentDetails", studentId);
                    return new StudentNotFoundException("Student not found with ID: " + studentId);
                });
        
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

	    logger.debug("Student details fetched for studentId: {} — courses: {}, schedules: {}, upcoming: {}, completed: {}",
                studentId, courseDtos.size(), allSchedules.size(), upcoming, completed);

	    return dto;

	}

	
	@Override
	public List<ClassAdminStudentDetailsResponse> viewStudents() {

		  logger.info("Fetching details for all students");
	    List<Student> students = studentRepository.findAll();

	    logger.debug("Total students found: {}", students.size());

	    return students.stream()
	            .map(s -> viewStudentDetails(s.getStudentId()))
	            .toList();
	}

    @Override
    public List<ClassScheduleResponse> getSchedulesByStaffId(String staffId) {


        logger.info("Fetching schedules for staffId: {}", staffId);
    	
        List<ClassSchedule> schedules = classScheduleRepository.findByStaff_StaffId(staffId);

        
        logger.debug("Found {} schedule(s) for staffId: {}", schedules.size(), staffId);
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

        logger.info("Adding {} topic(s) to batchId: {}", request.getTopics().size(), batchId);

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> {
                    logger.warn("ClassBatch not found with id: {} during addTopicsToClass", batchId);
                    return new ResourceNotFoundException("ClassBatch not found with id: " + batchId);
                });
        
        for (AddClassTopicRequest.TopicItem item : request.getTopics()) {

            boolean exists = classTopicRepository
                    .existsByClassBatchIdAndTopicId(batchId, item.getTopicId());

            if (exists) {
            	 logger.debug("TopicId: {} already exists in batchId: {}, skipping", item.getTopicId(), batchId);
                continue;
            }

            Topic topic = topicRepository.findById(item.getTopicId())
                    .orElseThrow(() -> {
                        logger.warn("Topic not found with id: {} during addTopicsToClass", item.getTopicId());
                        return new ResourceNotFoundException("Topic not found with id: " + item.getTopicId());
                    });
            
            ClassTopic classTopic = new ClassTopic();
            classTopic.setClassBatch(classBatch);
            classTopic.setTopic(topic);
            classTopic.setStatus(item.getStatus());

            classTopicRepository.save(classTopic);
            logger.debug("TopicId: {} added successfully to batchId: {}", item.getTopicId(), batchId);
        }
        logger.info("Topics processing completed for batchId: {}", batchId);
    }

    @Override
    public void removeTopicsFromClass(Long batchId, RemoveClassTopicRequest request) {

    	 logger.info("Removing {} topic(s) from batchId: {}", request.getTopicIds().size(), batchId);

         classBatchRepository.findById(batchId)
                 .orElseThrow(() -> {
                     logger.warn("ClassBatch not found with id: {} during removeTopicsFromClass", batchId);
                     return new ResourceNotFoundException("ClassBatch not found with id: " + batchId);
                 });
         
         
        classTopicRepository.deleteByClassBatchIdAndTopicIdIn(batchId,
                request.getTopicIds());
        
        logger.info("Topics removed successfully from batchId: {}", batchId);
    }

    @Override
    public List<ClassTopicResponse> getTopicsByBatchId(Long batchId) {

    	  logger.info("Fetching topics for batchId: {}", batchId);

          classBatchRepository.findById(batchId)
                  .orElseThrow(() -> {
                      logger.warn("ClassBatch not found with id: {} during getTopicsByBatchId", batchId);
                      return new ResourceNotFoundException("ClassBatch not found with id: " + batchId);
                  });
          
          
        List<ClassTopic> classTopics =
                classTopicRepository.findByClassBatchId(batchId);

        logger.debug("Found {} topic(s) for batchId: {}", classTopics.size(), batchId);
        return classTopicMapper.toResponseList(classTopics);
    }

    @Override
    public StudentCourseResponse assignCourseToStudent(String studentId, String courseId) {
    	  logger.info("Assigning courseId: {} to studentId: {}", courseId, studentId);

          Student student = studentRepository.findByStudentId(studentId)
                  .orElseThrow(() -> {
                      logger.warn("Student not found with id: {} during assignCourseToStudent", studentId);
                      return new StudentNotFoundException("Student not found: " + studentId);
                  });

          Course course = courseRepository.findByCourseId(courseId)
                  .orElseThrow(() -> {
                      logger.warn("Course not found with courseId: {} during assignCourseToStudent", courseId);
                      return new CourseNotFoundException("Course not found: " + courseId);
                  });
          
          
        // Check if already assigned
        boolean exists = studentCourseRepository
                .existsByStudent_StudentIdAndCourse_CourseId(
                        studentId,
                        courseId);

        if (exists) {
        	 logger.warn("CourseId: {} is already assigned to studentId: {}", courseId, studentId);
            throw new CourseAlreadyAssignedException("Course already assigned to student: " + courseId);
        }

        // Create enrollment
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourse.setStatus(CourseStatus.PLANNED);

        var saved = studentCourseRepository.save(studentCourse);

        logger.info("CourseId: {} assigned successfully to studentId: {}", courseId, studentId);
        return studentCourseMapper.toResponse(saved);
    }

}


