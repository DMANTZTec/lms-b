package com.dmantz.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // AUTH APIs
//                        .requestMatchers(
//                                "/api/auth/student/login",
//                                "/api/auth/staff/login"
//                        ).permitAll()

                        // CONTACT US PUBLIC APIs
                        .requestMatchers("/api/contact-us/**").permitAll()
                        .requestMatchers("/api/course/view-courses**").permitAll()


                        // STUDENT PUBLIC APIs
                        .requestMatchers(
                                "/api/student/register",
                                "/api/student/registration/verify-otp",
                                "/api/student/login",
                                "/api/student/verify-login-otp",
                                "/api/student/resend-otp",
                                "/api/student/*",
                                "/api/student/view-students",
                                "/api/student/forgot-password",
                                "/api/student/reset-password",
                                "/api/student-programs/**"
                        ).permitAll()

                        // Only ADMIN can create staff
                        .requestMatchers(HttpMethod.POST, "/api/staff/register")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/staff/active")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/staff/pagination")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/staff/profile-image")
                        .hasRole("ADMIN")

                        // STAFF PUBLIC APIs
                        .requestMatchers(
                                "/api/staff/admin-register",
                                "/api/staff/forgot-password",
                                "/api/staff/reset-password",

                                "/api/staff/set-password",
                                "/api/staff/login",
                                "/api/staff/login-verification-otp",
                                "/api/staff/*",
                                "/api/staff/resend-login-otp"
                        ).permitAll()


//                        // Only ADMIN can create staff/instructors
//                        .requestMatchers(
//                                "/api/staff/register"
//                        ).hasRole("ADMIN")

                        // SWAGGER APIs
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // STUDENT PROTECTED APIs
                        .requestMatchers("/api/student/**")
                        .hasRole("STUDENT")

                        // STAFF PROTECTED APIs
                        .requestMatchers("/api/staff/**")
                        .hasAnyRole("STAFF", "ADMIN", "FACULTY")

                        .anyRequest().authenticated())

                .addFilterBefore(
                        new JwtFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}

