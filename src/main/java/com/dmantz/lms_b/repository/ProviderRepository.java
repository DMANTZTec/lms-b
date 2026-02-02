package com.dmantz.lms_b.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms_b.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider,Long>{
	
	  // For CREATE
    @Query("""
        select count(p) > 0
        from Provider p
        where p.provider_nm = :providerName
    """)
    boolean existsByProviderName(@Param("providerName") String providerName);
	// For UPDATE (exclude current record)
    @Query("""
        select count(p) > 0
        from Provider p
        where p.provider_nm = :providerName
          and p.id <> :providerId
    """)
    boolean existsByProviderNameAndNotId(
            @Param("providerName") String providerName,
            @Param("providerId") Long providerId
    );
}
