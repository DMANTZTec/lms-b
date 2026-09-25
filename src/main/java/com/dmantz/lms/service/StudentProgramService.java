package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.AssignProgramRequest;
import com.dmantz.lms.dto.response.AssignProgramResponse;

public interface StudentProgramService {
    AssignProgramResponse assignProgramToStudent(AssignProgramRequest request);
}

