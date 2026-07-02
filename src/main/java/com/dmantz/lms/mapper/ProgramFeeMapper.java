package com.dmantz.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.dmantz.lms.dto.response.ProgramFeeHistoryResponse;
import com.dmantz.lms.dto.response.ProgramFeeSettingResponse;
import com.dmantz.lms.entity.Program;
import com.dmantz.lms.entity.ProgramFee;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgramFeeMapper {

	@Mapping(source = "effectiveDate", target = "effectiveDate")
	@Mapping(source = "fee", target = "fee")
	@Mapping(source = "discount", target = "discount")
	@Mapping(source = "duration", target = "duration")
	@Mapping(expression = "java(programFee.getDuration() != null ? programFee.getDuration().getLabel() : null)", target = "durationLabel")
	@Mapping(expression = "java(programFee.getSetBy() != null ? programFee.getSetBy().getFirstNm() + ' ' + programFee.getSetBy().getLastNm() : null)", target = "setBy")
	ProgramFeeHistoryResponse toHistoryResponse(ProgramFee programFee);

	@Mapping(source = "programId", target = "programId")
	@Mapping(source = "programTitle", target = "programTitle")
	void updateSettingFromProgram(Program program, @MappingTarget ProgramFeeSettingResponse response);

	default ProgramFeeSettingResponse toSettingResponse(Program program, List<ProgramFee> feeRecords) {
		List<ProgramFeeHistoryResponse> history = new ArrayList<>();
		for (ProgramFee feeRecord : feeRecords) {
			history.add(toHistoryResponse(feeRecord));
		}

		ProgramFeeSettingResponse response = new ProgramFeeSettingResponse();
		updateSettingFromProgram(program, response);
		response.setFeeHistory(history);
		response.setTotalHistoryRecords(history.size());

		ProgramFeeHistoryResponse currentFee = history.isEmpty() ? null : history.get(history.size() - 1);
		response.setCurrentFee(currentFee);

		if (currentFee != null && currentFee.getDurationLabel() != null) {
			response.setDuration(currentFee.getDurationLabel());
		}

		return response;
	}
}