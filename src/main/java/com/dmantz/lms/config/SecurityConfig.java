package com.dmantz.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtUtil jwtUtil;

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						// AUTH APIs
						.requestMatchers("/api/auth/student/login", "/api/auth/staff/login").permitAll()

						// SWAGGER APIs
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

						// STUDENT REGISTER
						.requestMatchers("/api/student/register").permitAll()

						// FIRST ADMIN REGISTER
						.requestMatchers("/api/staff/admin-register").permitAll()

						// STAFF REGISTER - ADMIN ONLY
						.requestMatchers("/api/staff/register").hasRole("ADMIN")

						// STUDENT APIs
						.requestMatchers("/api/student/**").hasRole("STUDENT")

						// STAFF APIs
						.requestMatchers("/api/staff/**").hasAnyRole("STAFF", "ADMIN", "FACULTY")

						.anyRequest().authenticated())

				.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}