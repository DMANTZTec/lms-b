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

	// ================= CREATE =================
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "providerName", target = "providerName")
	@Mapping(source = "providerOrgName", target = "providerOrgName")
	Provider toEntity(ProviderRequest request);

	// ================= RESPONSE =================
	@Mapping(source = "providerName", target = "providerName")
	@Mapping(source = "providerOrgName", target = "providerOrgName")
	@Mapping(source = "createdBy", target = "createdBy")
	@Mapping(source = "createdDt", target = "createdDt")
	@Mapping(source = "updatedBy", target = "updatedBy")
	@Mapping(source = "updatedDt", target = "updatedDt")
	ProviderResponse toResponse(Provider provider);

	// ================= UPDATE =================
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "providerName", target = "providerName")
	@Mapping(source = "providerOrgName", target = "providerOrgName")
	void updateEntityFromRequest(ProviderRequest request, @MappingTarget Provider provider);
}
