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

		http
				.csrf(csrf -> csrf.disable())

				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						//SWAGGER
						.requestMatchers(
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/v3/api-docs/**"
						).permitAll()

						//AUTH APIs
						.requestMatchers("/api/auth/student/login",
								"/api/auth/staff/login").permitAll()

						//STUDENT PUBLIC APIs
						.requestMatchers(
								"/api/student/register",
								"/api/student/login",
								"/api/student/otp-verify",
								"/api/student/verify-registration-otp"
						).permitAll()

						//STAFF PUBLIC APIs
						.requestMatchers(
								"/api/staff/admin-register",
								"/api/staff/verify-otp",
								"/api/staff/forgot-password",
								"/api/staff/reset-password"
						).permitAll()

						//STAFF ADMIN APIs
						.requestMatchers("/api/staff/register")
						.hasRole("ADMIN")

						//STUDENT PROTECTED APIs
						.requestMatchers("/api/student/**")
						.hasRole("STUDENT")

						// STAFF PROTECTED APIs
						.requestMatchers("/api/staff/**")
						.hasAnyRole("STAFF", "ADMIN", "FACULTY")

						// EVERYTHING ELSE
						.anyRequest().authenticated()
				)

				.addFilterBefore(
						new JwtFilter(jwtUtil),
						UsernamePasswordAuthenticationFilter.class
				);

		return http.build();
	}
}