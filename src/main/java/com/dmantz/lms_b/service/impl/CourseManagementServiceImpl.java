package com.dmantz.lms_b.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.*;
import com.dmantz.lms_b.entity.*;
import com.dmantz.lms_b.mapper.*;
import com.dmantz.lms_b.repository.*;
import org.springframework.stereotype.Service;

import com.dmantz.lms_b.exceptions.DuplicateValuesException;
import com.dmantz.lms_b.exceptions.ResourceNotFoundException;
import com.dmantz.lms_b.service.CourseManagementService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseManagementServiceImpl implements CourseManagementService {

	private final SubjectRepository subjectRepository;
	private final StaffRepository staffRepository;
	private final SubjectMapper subjectMapper;

	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
	private final ProviderRepository providerRepository;

	private final ChapterRepository chapterRepository;
	private final ChapterMapper chapterMapper;

	private final TopicRepository topicRepository;
	private final TopicMapper topicMapper;

	private final TopicReferenceRepository topicReferenceRepository;
	private final TopicReferenceMapper topicReferenceMapper;

	private final ProgramRepository programRepository;
	private final ProgramCourseRepository programCourseRepository;
	private final ProgramCourseMapper programcourseMapper;
	private final ProgramMapper programMapper;

	public CourseManagementServiceImpl(SubjectRepository subjectRepository, StaffRepository staffRepository,
			SubjectMapper subjectMapper, CourseRepository courseRepository, CourseMapper courseMapper,
			ProviderRepository providerRepository, ChapterRepository chapterRepository, ChapterMapper chapterMapper,
			TopicRepository topicRepository, TopicMapper topicMapper, TopicReferenceRepository topicReferenceRepository,
			TopicReferenceMapper topicReferenceMapper, ProgramRepository programRepository,
			ProgramCourseRepository programCourseRepository, ProgramCourseMapper programcourseMapper,
			ProgramMapper programMapper) {
		super();
		this.subjectRepository = subjectRepository;
		this.staffRepository = staffRepository;
		this.subjectMapper = subjectMapper;
		this.courseRepository = courseRepository;
		this.courseMapper = courseMapper;
		this.providerRepository = providerRepository;
		this.chapterRepository = chapterRepository;
		this.chapterMapper = chapterMapper;
		this.topicRepository = topicRepository;
		this.topicMapper = topicMapper;
		this.topicReferenceRepository = topicReferenceRepository;
		this.topicReferenceMapper = topicReferenceMapper;
		this.programRepository = programRepository;
		this.programCourseRepository = programCourseRepository;
		this.programcourseMapper = programcourseMapper;
		this.programMapper = programMapper;
	}

	// ------------------ CREATE SUBJECT ------------------
	@Override
	public SubjectResponse createSubject(SubjectRequest requestDto, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Check duplicate short code
		subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd()).ifPresent(existing -> {
			throw new DuplicateValuesException("Subject already exists with code: " + requestDto.getSubjectShortCd());
		});

		Subject subject = subjectMapper.toEntity(requestDto);

		Subject savedSubject = subjectRepository.save(subject);
		return subjectMapper.toDto(savedSubject);
	}

	// ------------------ VIEW ALL SUBJECTS ------------------
	@Override
	public List<SubjectResponse> viewAllSubjects() {
		return subjectRepository.findAll().stream().map(subjectMapper::toDto).collect(Collectors.toList());
	}

	// ------------------ UPDATE SUBJECT ------------------
	@Override
	public SubjectResponse updateSubject(Long subjectId, SubjectRequest requestDto, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch subject
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

		// Check duplicate short code (excluding same subject)
		subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd()).ifPresent(existing -> {
			if (!existing.getId().equals(subjectId)) {
				throw new DuplicateValuesException(
						"Another subject already exists with code: " + requestDto.getSubjectShortCd());
			}
		});

		// Update fields using MapStruct
		subjectMapper.updateSubjectFromRequest(requestDto, subject);
		Subject updatedSubject = subjectRepository.save(subject);
		return subjectMapper.toDto(updatedSubject);
	}

	// -------------------------DELETE SUBJECT-----------------------------
	@Override
	public void deleteSubject(Long subjectId, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff not found with id: " + staffId);
		}

		// Fetch subject
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
		subjectRepository.delete(subject);
	}

//  ----------------------------CREATE COURSE--------------------
	@Override
	public CourseResponse createCourse(CourseRequest requestDto, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch Subject
		Subject subject = subjectRepository.findById(requestDto.getSubjectId()).orElseThrow(
				() -> new ResourceNotFoundException("Subject not found with ID: " + requestDto.getSubjectId()));

		// Fetch Provider
		Provider provider = providerRepository.findById(requestDto.getProviderId()).orElseThrow(
				() -> new ResourceNotFoundException("Provider not found with ID: " + requestDto.getProviderId()));

		// Check duplicate
		boolean exists = courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
				requestDto.getCourseTitle(), subject.getId(), provider.getId(), requestDto.getLanguage());

		if (exists) {
			throw new DuplicateValuesException("Course already exists for this subject, provider, and language");
		}

		// Generate courseId
		String courseId = generateCourseId(subject);

		// Map request DTO → entity
		Course course = courseMapper.toEntity(requestDto);
		course.setCourseId(courseId);
		course.setSubject(subject);
		course.setProvider(provider);

		// Save
		Course savedCourse = courseRepository.save(course);

		// Return response
		return courseMapper.toDto(savedCourse);
	}

	private String generateCourseId(Subject subject) {

		String subjectShortCd = subject.getSubjectShortCd();

		if (subjectShortCd == null || subjectShortCd.isBlank()) {
			throw new IllegalStateException("Subject short code is required");
		}

		long count = courseRepository.countBySubject_SubjectShortCd(subjectShortCd) + 1;

		return String.format("%s%03d", subjectShortCd.toUpperCase(), count);
	}

	// ------------------ VIEW ALL COURSES ------------------
	@Override
	public List<CourseResponse> viewAllCourses() {
		return courseRepository.findAll().stream().map(courseMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public CourseResponse updateCourse(Long courseId, CourseRequest request, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch course
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

		// Fetch Subject
		Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(
				() -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));

		// Fetch Provider
		Provider provider = providerRepository.findById(request.getProviderId()).orElseThrow(
				() -> new ResourceNotFoundException("Provider not found with id: " + request.getProviderId()));

		// Check duplicate course
		boolean exists = courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
				request.getCourseTitle(), subject.getId(), provider.getId(), request.getLanguage());

		if (exists && !(course.getCourseTitle().equals(request.getCourseTitle())
				&& course.getSubject().getId().equals(subject.getId())
				&& course.getProvider().getId().equals(provider.getId())
				&& course.getLanguage().equals(request.getLanguage()))) {

			throw new DuplicateValuesException(
					"Another course already exists with same title, subject, provider and language");
		}

		// Update fields using MapStruct
		courseMapper.updateCourseFromRequest(request, course);

		// Update relations
		course.setSubject(subject);
		course.setProvider(provider);

		Course updatedCourse = courseRepository.save(course);
		return courseMapper.toDto(updatedCourse);
	}

	@Override
	public void deleteCourse(Long courseId, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff not found with id: " + staffId);
		}

		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

		courseRepository.delete(course);
	}

	@Override
	public List<CourseResponse> viewCoursesBySubject(Long subjectId) {
		// Validate subject exists
		if (!subjectRepository.existsById(subjectId)) {
			throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
		}

		return courseRepository.findBySubject_Id(subjectId).stream().map(courseMapper::toDto)
				.collect(Collectors.toList());
	}

	// ================= CREATE CHAPTER=================
	@Override
	public ChapterResponse createChapter(Long staffId, ChapterRequest request) {

		Staff staff = staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Course course = courseRepository.findByCourseId(request.getCourseId())
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

		chapterRepository.findByCourse_CourseIdAndChapterNmIgnoreCase(request.getCourseId(), request.getChapterNm())
				.ifPresent(ch -> {
					throw new DuplicateValuesException("Chapter name already exists in this course");
				});

		Long nextChapterNum = chapterRepository.findTopByCourse_CourseIdOrderByChapterNumDesc(request.getCourseId())
				.map(ch -> ch.getChapterNum() + 1).orElse(1L);
		Chapter chapter = chapterMapper.toEntity(request);
		chapter.setCourse(course); // ✅ managed entity
		chapter.setChapterNum(nextChapterNum);
		Chapter savedChapter = chapterRepository.save(chapter);

		return chapterMapper.toResponse(savedChapter);
	}

	// ================= GET CHAPTER BY ID =================
	@Override
	public ChapterResponse getChapterById(Long chapterId) {
		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		return chapterMapper.toResponse(chapter);
	}

	// ================= GET ALL CHAPTERS =================
	@Override
	public List<ChapterResponse> getAllChapters() {
		return chapterRepository.findAll().stream().map(chapterMapper::toResponse).toList();
	}

	// ================= UPDATE CHAPTER =================
	@Override
	public ChapterResponse updateChapter(Long chapterId, ChapterRequest request, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + chapterId));

		Course course = null;
		if (request.getCourseId() != null) {
			course = courseRepository.findByCourseId(request.getCourseId()).orElseThrow(
					() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
		}

		String courseIdToCheck = (course != null) ? course.getCourseId() : chapter.getCourse().getCourseId();

		chapterRepository.findByCourse_CourseIdAndChapterNmIgnoreCase(courseIdToCheck, request.getChapterNm())
				.filter(ch -> !ch.getId().equals(chapterId)).ifPresent(ch -> {
					throw new DuplicateValuesException("Chapter name already exists in this course");
				});

		chapterMapper.updateEntityFromRequest(request, chapter);
		Chapter updatedChapter = chapterRepository.save(chapter);

		return chapterMapper.toResponse(updatedChapter);
	}

	// ================= DELETE CHAPTER=================
	@Override
	public void deleteChapter(Long chapterId, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + chapterId));

		chapterRepository.delete(chapter);
	}

	// ================== CREATE A TOPIC ===================

	@Override
	public TopicResponseDto createTopic(TopicRequestDto request) {

		Chapter chapter = chapterRepository.findById(request.getChapterId()).orElseThrow(
				() -> new ResourceNotFoundException("Chapter not found with id: " + request.getChapterId()));

		Staff staff = staffRepository.findById(request.getStaffId())
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + request.getStaffId()));

		Long maxTopicNum = topicRepository.findMaxTopicNumByChapterId(request.getChapterId());

		Long nextTopicNum = (maxTopicNum == null ? 1L : maxTopicNum + 1);

		Topic topic = topicMapper.toEntity(request);

		topic.setChapter(chapter);
		topic.setTopicNum(nextTopicNum);
		Topic savedTopic = topicRepository.save(topic);

		return topicMapper.toResponseDto(savedTopic);
	}

	// ================= Get all Topics by ChapterId ============================

	@Override
	public List<TopicResponseDto> getTopicsByChapterId(Long chapterId) {

		chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + chapterId));

		List<Topic> topics = topicRepository.findByChapterIdOrderByTopicNumAsc(chapterId);

		return topics.stream().map(topicMapper::toResponseDto).collect(Collectors.toList());
	}

	// ====================== Get Topic by Id and Chapter Id
	// =========================

	@Override
	public TopicResponseDto getTopicByIdAndChapterId(Long topicId, Long chapterId) {

		Topic topic = topicRepository.findByIdAndChapterId(topicId, chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found in this chapter"));

		return topicMapper.toResponseDto(topic);
	}

	// ======================== Update Topic ==================================

	@Override
	public TopicResponseDto updateTopic(Long id, TopicRequestDto requestDto) {

		Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("Topic not found"));

		Chapter chapter = chapterRepository.findById(requestDto.getChapterId())
				.orElseThrow(() -> new RuntimeException("Chapter not found"));

		Staff staff = staffRepository.findById(requestDto.getStaffId())
				.orElseThrow(() -> new RuntimeException("Staff not found"));

		topic.setTopicNm(requestDto.getTopicName());
		topic.setDescription(requestDto.getDescription());
		topic.setExpectedTimeMin(requestDto.getExpectedTimeMin());
		topic.setChapter(chapter);

		Topic updatedTopic = topicRepository.save(topic);

		return topicMapper.toResponseDto(updatedTopic);
	}

	// ============================= Delete Topic
	// ===================================

	@Override
	public void deleteTopic(Long id) {

		Topic topic = topicRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

		topicRepository.delete(topic);
	}
//      =====================  Move chapter ========================================

	@Override
	public void moveChapter(Long chapterId, int targetPosition) {

		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new RuntimeException("Chapter not found"));

		Long courseId = chapter.getCourse().getId();

		List<Chapter> chapters = chapterRepository.findByCourseIdOrderByChapterNumAsc(courseId);

		int size = chapters.size();

		if (targetPosition < 1 || targetPosition > size) {
			throw new RuntimeException("Invalid target position");
		}

		// Remove old position
		chapters.removeIf(ch -> ch.getId().equals(chapterId));

		// Insert at new position
		chapters.add(targetPosition - 1, chapter);

		// 🔥 STEP 1 — Shift all values temporarily
		for (Chapter ch : chapters) {
			ch.setChapterNum(ch.getChapterNum() + 1000);
		}

		chapterRepository.saveAll(chapters);
		chapterRepository.flush();

		// 🔥 STEP 2 — Reassign correct order
		for (int i = 0; i < chapters.size(); i++) {
			chapters.get(i).setChapterNum((long) (i + 1));
		}

		chapterRepository.saveAll(chapters);
	}
//  =====================  Move topic ========================================

	@Override
	public void moveTopic(Long topicId, int targetPosition) {

		// 1️⃣ Fetch topic
		Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));

		Long chapterId = topic.getChapter().getId();

		// 2️⃣ Fetch all topics ordered
		List<Topic> topics = topicRepository.findByChapterIdOrderByTopicNumAsc(chapterId);

		int size = topics.size();

		if (targetPosition < 1 || targetPosition > size) {
			throw new RuntimeException("Invalid target position");
		}

		// 3️⃣ Remove current topic
		topics.removeIf(t -> t.getId().equals(topicId));

		// 4️⃣ Insert at new position
		topics.add(targetPosition - 1, topic);

		// 🔥 STEP 1: Temporary shift to avoid UNIQUE conflict
		for (Topic t : topics) {
			t.setTopicNum(t.getTopicNum() + 1000);
		}

		topicRepository.saveAll(topics);
		topicRepository.flush();

		// 🔥 STEP 2: Reassign correct order
		for (int i = 0; i < topics.size(); i++) {
			topics.get(i).setTopicNum((long) (i + 1));
		}

		topicRepository.saveAll(topics);
	}

// =============================== Add Topic References ======================================
	@Override
	public TopicReferenceResponseDto addUrlReference(Long topicId, TopicReferenceRequestDto dto) {

		return saveReference(topicId, dto, "URL");
	}

	@Override
	public TopicReferenceResponseDto addVideoReference(Long topicId, TopicReferenceRequestDto dto) {

		return saveReference(topicId, dto, "VIDEO");
	}

	@Override
	public TopicReferenceResponseDto addDocumentReference(Long topicId, TopicReferenceRequestDto dto) {

		return saveReference(topicId, dto, "DOCUMENT");
	}

	private TopicReferenceResponseDto saveReference(Long topicId, TopicReferenceRequestDto dto, String type) {

		Topic topic = topicRepository.findById(topicId)
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

		TopicReference entity = topicReferenceMapper.toEntity(dto);
		entity.setRefType(type);
		entity.setTopic(topic);

		TopicReference saved = topicReferenceRepository.save(entity);

		return topicReferenceMapper.toDto(saved);
	}

//	  create program
	@Override
	public ProgramResponse createProgram(ProgramRequest request) {

		Provider provider = providerRepository.findById(request.getProviderId())
				.orElseThrow(() -> new RuntimeException("Provider not found"));
		if (programRepository.existsByProgramTitleAndProvider_Id(request.getProgramTitle(), request.getProviderId())) {
			throw new DuplicateValuesException("Program already exists for this provider");
		}

		Program program = programMapper.toEntity(request);

		program.setProvider(provider);

		program.setProgramId(generateProgramId());

		program.setStatus(ProgramStatus.ACTIVE);

		Program savedProgram = programRepository.save(program);

		return programMapper.toResponse(savedProgram);
	}

	private String generateProgramId() {

		Optional<Program> lastProgram = programRepository.findTopByOrderByIdDesc();

		int nextNumber = 1;

		if (lastProgram.isPresent() && lastProgram.get().getProgramId() != null) {
			String lastId = lastProgram.get().getProgramId();
			nextNumber = Integer.parseInt(lastId.substring(3)) + 1;
		}

		String generatedId;

		do {
			generatedId = String.format("PRG%03d", nextNumber++);
		} while (programRepository.existsByProgramId(generatedId));

		return generatedId;
	}

	// ================= GET BY ID =================
	@Override
	public ProgramResponse getProgramById(Long id) {

		Program program = programRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + id));

		return programMapper.toResponse(program);
	}

	// ================= GET ALL =================
	@Override
	public List<ProgramResponse> getAllPrograms() {

		return programRepository.findAll().stream().map(programMapper::toResponse).toList();
	}

	// ================= UPDATE =================
	@Override
	public ProgramResponse updateProgram(Long programId, ProgramRequest request) {

		// Validate Program
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + programId));

		// Validate Provider
		Provider provider = providerRepository.findById(request.getProviderId()).orElseThrow(
				() -> new ResourceNotFoundException("Provider not found with id: " + request.getProviderId()));

		// Duplicate check
		if (programRepository.existsByProgramTitleAndProvider_IdAndIdNot(request.getProgramTitle(),
				request.getProviderId(), programId)) {
			throw new DuplicateValuesException("Program already exists for this provider");
		}

		// Update entity (excluding programId)
		programMapper.updateEntityFromRequest(request, program);
		program.setProvider(provider);

		Program updatedProgram = programRepository.save(program);

		return programMapper.toResponse(updatedProgram);
	}

	// ================= DELETE =================
	@Override
	public void deleteProgram(Long programId) {
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + programId));

		programRepository.delete(program);
	}

//	========================= Add course to program =============================
	@Override
	public List<ProgramCourseResponse> addCoursesToProgram(ProgramCourseRequest request) {

		String programId = request.getProgramId();
		List<String> courseIds = request.getCourseIds();

		Program program = programRepository.findByProgramId(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with ID: " + programId));

		List<ProgramCourse> savedList = new ArrayList<>();

		for (String courseId : courseIds) {

			Course course = courseRepository.findByCourseId(courseId)
					.orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

			boolean exists = programCourseRepository.existsByProgram_ProgramIdAndCourse_CourseId(programId, courseId);

			if (exists) {
				throw new DuplicateValuesException("Course " + courseId + " is already mapped to Program " + programId);
			}

			ProgramCourse programCourse = new ProgramCourse();
			programCourse.setProgram(program);
			programCourse.setCourse(course);

			savedList.add(programCourseRepository.save(programCourse));
		}

		return programcourseMapper.toResponseList(savedList);
	}

	// ========================= delete course from program
	@Override
	public void deleteProgramCourse(Long programCourseId) {

		ProgramCourse programCourse = programCourseRepository.findById(programCourseId).orElseThrow(
				() -> new ResourceNotFoundException("ProgramCourse not found with id: " + programCourseId));

		programCourseRepository.delete(programCourse);
	}

}
