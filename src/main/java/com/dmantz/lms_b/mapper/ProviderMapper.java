package com.dmantz.lms_b.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms_b.dto.request.ProviderRequest;
import com.dmantz.lms_b.dto.response.ProviderResponse;
import com.dmantz.lms_b.entity.Provider;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    // Request → Entity (CREATE)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "providerName", target = "provider_nm")
    @Mapping(source = "providerOrgName", target = "provider_org_nm")
    Provider toEntity(ProviderRequest request);

    // Entity → Response
    @Mapping(source = "provider_nm", target = "providerName")
    @Mapping(source = "provider_org_nm", target = "providerOrgName")
    @Mapping(source = "created_by", target = "createdBy")
    @Mapping(source = "created_dt", target = "createdDt")
    @Mapping(source = "updated_by", target = "updatedBy")
    @Mapping(source = "updated_dt", target = "updatedDt")
    ProviderResponse toResponse(Provider provider);

    // Update existing entity (UPDATE)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "providerName", target = "provider_nm")
    @Mapping(source = "providerOrgName", target = "provider_org_nm")
    void updateEntityFromRequest(
            ProviderRequest request,
            @MappingTarget Provider provider
    );
}
