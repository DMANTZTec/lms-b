package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.AcknowledgeMentorRequest;
import com.dmantz.lms.dto.request.StudentTaskMentorRequest;
import com.dmantz.lms.dto.request.UpdateMentorMinutesRequest;
import com.dmantz.lms.dto.response.StudentTaskMentorResponse;

public interface StudentTaskMentorService {
	
	public StudentTaskMentorResponse createMentoringActivity(
            StudentTaskMentorRequest request);
    public StudentTaskMentorResponse updateMentoringMinutes(
            Long id, UpdateMentorMinutesRequest request);
    public StudentTaskMentorResponse acknowledgeMentorHelp(
            Long id);
}