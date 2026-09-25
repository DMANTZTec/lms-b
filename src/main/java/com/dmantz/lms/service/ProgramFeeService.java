package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.ProgramFeeRequest;
import com.dmantz.lms.dto.response.ProgramFeeHistoryResponse;
import com.dmantz.lms.dto.response.ProgramFeeSettingResponse;

public interface ProgramFeeService {

    ProgramFeeSettingResponse getProgramFeeSetting(String programId);

    ProgramFeeHistoryResponse createProgramFee(String programId, ProgramFeeRequest request, String staffId);

    ProgramFeeSettingResponse updateProgramFee(String programId, ProgramFeeRequest request, String staffId);

    List<ProgramFeeHistoryResponse> getFeeHistory(String programId);
}