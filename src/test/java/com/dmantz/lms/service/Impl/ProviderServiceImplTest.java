
package com.dmantz.lms.service.Impl;

import com.dmantz.lms.dto.request.ProviderRequest;
import com.dmantz.lms.dto.response.ProviderResponse;
import com.dmantz.lms.entity.Provider;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.ProviderMapper;
import com.dmantz.lms.repository.ProviderRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.impl.ProviderServiceImpl;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

public class ProviderServiceImplTest {
	@Mock
	private ProviderRepository providerRepository;
	@Mock
	private ProviderMapper providerMapper;
	@Mock
	private StaffRepository staffRepository;
	private ProviderServiceImpl service;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.openMocks(this);
		service = new ProviderServiceImpl(providerRepository, providerMapper, staffRepository);
	}

	// ================= CREATE =================
	@Test
	public void testCreateProviderSuccess() {
		ProviderRequest request = new ProviderRequest();
		request.setProviderName("Test Provider");
		Staff staff = new Staff();
		Provider provider = new Provider();
		ProviderResponse response = new ProviderResponse();
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(staff));
		when(providerRepository.existsByProviderName(any())).thenReturn(false);
		when(providerMapper.toEntity(any())).thenReturn(provider);
		when(providerRepository.save(any())).thenReturn(provider);
		when(providerMapper.toResponse(any())).thenReturn(response);
		ProviderResponse result = service.createProvider(request, 1L);
		assertNotNull(result);
		verify(providerRepository).save(provider);
	}

	@Test(expectedExceptions = ResourceNotFoundException.class)
	public void testCreateProviderStaffNotFound() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());
		service.createProvider(new ProviderRequest(), 1L);
	}

	@Test(expectedExceptions = DuplicateValuesException.class)
	public void testCreateProviderDuplicateName() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(new Staff()));
		when(providerRepository.existsByProviderName(any())).thenReturn(true);
		service.createProvider(new ProviderRequest(), 1L);
	}

	// ================= GET BY ID =================
	@Test
	public void testGetProviderByIdSuccess() {
		Provider provider = new Provider();
		ProviderResponse response = new ProviderResponse();
		when(providerRepository.findById(anyLong())).thenReturn(Optional.of(provider));
		when(providerMapper.toResponse(any())).thenReturn(response);
		ProviderResponse result = service.getProviderById(1L);
		assertNotNull(result);
	}

	@Test(expectedExceptions = ResourceNotFoundException.class)
	public void testGetProviderByIdNotFound() {
		when(providerRepository.findById(anyLong())).thenReturn(Optional.empty());
		service.getProviderById(1L);
	}

	// ================= GET ALL =================
	@Test
	public void testGetAllProvidersSuccess() {
		List<Provider> providers = Arrays.asList(new Provider(), new Provider());
		when(providerRepository.findAll()).thenReturn(providers);
		when(providerMapper.toResponse(any())).thenReturn(new ProviderResponse());
		List<ProviderResponse> result = service.getAllProviders();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetAllProvidersEmpty() {
		when(providerRepository.findAll()).thenReturn(Collections.emptyList());
		List<ProviderResponse> result = service.getAllProviders();
		assertTrue(result.isEmpty());
	}

	// ================= UPDATE =================
	@Test
	public void testUpdateProviderSuccess() {
		ProviderRequest request = new ProviderRequest();
		Staff staff = new Staff();
		Provider provider = new Provider();
		ProviderResponse response = new ProviderResponse();
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(staff));
		when(providerRepository.findById(anyLong())).thenReturn(Optional.of(provider));
		when(providerRepository.existsByProviderNameAndIdNot(any(), anyLong())).thenReturn(false);
		when(providerRepository.save(any())).thenReturn(provider);
		when(providerMapper.toResponse(any())).thenReturn(response);
		ProviderResponse result = service.updateProvider(1L, request, 1L);
		assertNotNull(result);
	}

	@Test(expectedExceptions = ResourceNotFoundException.class)
	public void testUpdateProviderStaffNotFound() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());
		service.updateProvider(1L, new ProviderRequest(), 1L);
	}

	@Test(expectedExceptions = ResourceNotFoundException.class)
	public void testUpdateProviderNotFound() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(new Staff()));
		when(providerRepository.findById(anyLong())).thenReturn(Optional.empty());
		service.updateProvider(1L, new ProviderRequest(), 1L);
	}

	@Test(expectedExceptions = DuplicateValuesException.class)
	public void testUpdateProviderDuplicateName() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(new Staff()));
		when(providerRepository.findById(anyLong())).thenReturn(Optional.of(new Provider()));
		when(providerRepository.existsByProviderNameAndIdNot(any(), anyLong())).thenReturn(true);
		service.updateProvider(1L, new ProviderRequest(), 1L);
	}

	// ================= DELETE =================
	@Test
	public void testDeleteProviderSuccess() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(new Staff()));
		when(providerRepository.findById(anyLong())).thenReturn(Optional.of(new Provider()));
		service.deleteProvider(1L, 1L);
		verify(providerRepository).delete(any());
	}

	@Test(expectedExceptions = ResourceNotFoundException.class)
	public void testDeleteProviderStaffNotFound() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());
		service.deleteProvider(1L, 1L);
	}

	@Test(expectedExceptions = ResourceNotFoundException.class)
	public void testDeleteProviderNotFound() {
		when(staffRepository.findById(anyLong())).thenReturn(Optional.of(new Staff()));
		when(providerRepository.findById(anyLong())).thenReturn(Optional.empty());
		service.deleteProvider(1L, 1L);
	}
}
