package com.dmantz.lms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

	boolean existsByProviderName(String providerName);

	boolean existsByProviderNameAndIdNot(String providerName, Long id);
}
