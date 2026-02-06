package com.dmantz.lms_b.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms_b.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider,Long>{
	
	  
    boolean existsByProviderName(String providerName);

    boolean existsByProviderNameAndIdNot(String providerName, Long id);
}
