package com.dmantz.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            // JwtFilter resolves and caches the numeric staff id in
            // Authentication#details up front, outside any JPA flush, so this
            // never needs to query the DB itself (doing so here would run a
            // query mid-flush and break the surrounding transaction commit).
            Object details = authentication.getDetails();

            if (details instanceof Long staffId) {
                return Optional.of(staffId);
            }

            return Optional.empty();
        };
    }
}
