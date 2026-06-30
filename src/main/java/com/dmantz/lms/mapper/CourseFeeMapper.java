package com.dmantz.lms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmantz.lms.dto.response.CourseFeeHistoryResponse;
import com.dmantz.lms.entity.CourseFee;

@Mapper(componentModel = "spring")
public interface CourseFeeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "effectiveDate", target = "effectiveDate")
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "discount", target = "discount")
    CourseFeeHistoryResponse toHistoryResponse(CourseFee courseFee);

    default CourseFeeHistoryResponse toHistoryResponse(CourseFee courseFee, int serialNumber) {
        CourseFeeHistoryResponse response = toHistoryResponse(courseFee);
        response.setSerialNumber(serialNumber);
        return response;
    }
}