package com.dmantz.lms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms.dto.request.ProviderRequest;
import com.dmantz.lms.dto.response.ProviderResponse;
import com.dmantz.lms.entity.Provider;

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
	ProviderResponse toResponse(Provider provider);

	// ================= UPDATE =================
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "providerName", target = "providerName")
	@Mapping(source = "providerOrgName", target = "providerOrgName")
	void updateEntityFromRequest(ProviderRequest request, @MappingTarget Provider provider);
}
