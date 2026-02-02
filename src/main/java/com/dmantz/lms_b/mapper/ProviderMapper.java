package com.dmantz.lms_b.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms_b.dto.request.ProviderRequest;
import com.dmantz.lms_b.dto.response.ProviderResponse;
import com.dmantz.lms_b.entity.Provider;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    // Request → Entity (for create)
    @Mapping(source = "providerName", target = "provider_nm")
    @Mapping(source = "providerOrgName", target = "provider_org_nm")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "created_dt", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "updated_dt", ignore = true)
    Provider toEntity(ProviderRequest dto);

    // Entity → Response
    @Mapping(source = "provider_nm", target = "providerName")
    @Mapping(source = "provider_org_nm", target = "providerOrgName")
    @Mapping(source = "created_by", target = "createdBy")
    @Mapping(source = "created_dt", target = "createdDt")
    @Mapping(source = "updated_by", target = "updatedBy")
    @Mapping(source = "updated_dt", target = "updatedDt")
    ProviderResponse toResponse(Provider provider);

    // For UPDATE (update entity fields from DTO)
    @Mapping(source = "providerName", target = "provider_nm")
    @Mapping(source = "providerOrgName", target = "provider_org_nm")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_by", ignore = true)
    @Mapping(target = "created_dt", ignore = true)
    @Mapping(target = "updated_by", ignore = true)
    @Mapping(target = "updated_dt", ignore = true)
    void updateEntityFromDto(ProviderRequest dto, @MappingTarget Provider provider);
}
