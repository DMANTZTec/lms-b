package com.dmantz.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.dmantz.lms.dto.response.CourseFeeHistoryResponse;
import com.dmantz.lms.dto.response.CourseFeeSettingResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.CourseFee;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseFeeMapper {

    @Mapping(source = "effectiveDate", target = "effectiveDate")
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "discount", target = "discount")
    @Mapping(source = "courseDuration", target = "courseDuration")
    @Mapping(expression = "java(courseFee.getCourseDuration() != null ? courseFee.getCourseDuration().getLabel() : null)", target = "courseDurationLabel")
    CourseFeeHistoryResponse toHistoryResponse(CourseFee courseFee);

    List<CourseFeeHistoryResponse> toHistoryResponseList(List<CourseFee> feeRecords);

    @Mapping(source = "courseId", target = "courseId")
    @Mapping(source = "courseTitle", target = "courseTitle")
    @Mapping(source = "subject.subjectNm", target = "subjectNm")
    void updateSettingFromCourse(Course course, @MappingTarget CourseFeeSettingResponse response);

    default CourseFeeSettingResponse toSettingResponse(Course course, List<CourseFee> feeRecords) {
        List<CourseFeeHistoryResponse> history = new ArrayList<>();
        int serialNumber = 1;
        for (CourseFee feeRecord : feeRecords) {
            CourseFeeHistoryResponse h = toHistoryResponse(feeRecord);
            h.setSerialNumber(serialNumber++);
            history.add(h);
        }

        CourseFeeSettingResponse response = new CourseFeeSettingResponse();
        updateSettingFromCourse(course, response);
        response.setFeeHistory(history);
        response.setTotalHistoryRecords(history.size());

        CourseFeeHistoryResponse currentFee = history.isEmpty() ? null : history.get(history.size() - 1);
        response.setCurrentFee(currentFee);

        if (currentFee != null && currentFee.getCourseDurationLabel() != null) {
            response.setCourseDuration(currentFee.getCourseDurationLabel());
        }

        return response;
    }
}