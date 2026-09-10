package com.dmantz.lms.config;

import com.dmantz.lms.repository.StaffRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final StaffRepository staffRepository;

    public JwtFilter(JwtUtil jwtUtil, StaffRepository staffRepository) {
        this.jwtUtil = jwtUtil;
        this.staffRepository = staffRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip JWT validation for public APIs
        if (path.equals("/api/staff/login")
                || path.equals("/api/staff/admin-register")
                || path.equals("/api/staff/set-password")
                || path.equals("/api/staff/forgot-password")
                || path.equals("/api/staff/reset-password")
                || path.equals("/api/staff/login-verification-otp")
                || path.equals("/api/staff/resend-login-otp")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            try {

                String token = authHeader.substring(7);

                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                // Resolve the numeric staff id up front, outside any JPA flush, so
                // JpaAuditConfig's AuditorAware can read it without querying the DB
                // mid-flush (which caused "Could not commit JPA transaction" errors).
                if (!"STUDENT".equals(role)) {
                    staffRepository.findByEmailId(email)
                            .ifPresent(staff -> authentication.setDetails(staff.getId()));
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT Token Expired");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
