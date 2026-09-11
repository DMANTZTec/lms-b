package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.LearnerPathRequest;
import com.dmantz.lms.dto.response.LearnerPathResponse;
import com.dmantz.lms.entity.LearnerPath;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LearnerPathMapper {

    ObjectMapper mapper = new ObjectMapper();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "items", expression = "java(toJson(request.getItems()))")
    LearnerPath toEntity(LearnerPathRequest request);

    @Mapping(target = "items", expression = "java(fromJson(entity.getItems()))")
    LearnerPathResponse toResponse(LearnerPath entity);

    List<LearnerPathResponse> toResponseList(List<LearnerPath> entities);

    default String toJson(List<String> items) {
        try {
            if (items == null || items.isEmpty()) {
                return "[]";
            }
            return mapper.writeValueAsString(items);
        } catch (Exception e) {
            throw new RuntimeException("Error converting items to JSON", e);
        }
    }

    default List<String> fromJson(String items) {
        try {
            if (items == null || items.isBlank()) {
                return List.of();
            }
            return mapper.readValue(items, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting items from JSON", e);
        }
    }
}