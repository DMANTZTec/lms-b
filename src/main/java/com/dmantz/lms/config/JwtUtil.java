package com.dmantz.lms.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET = "mySuperSecretKeyForJwtRestroProject1234567890";

	public String generateToken(String email, String role, String userId) {

		return Jwts.builder().setSubject(email).claim("role", role).claim("userId", userId).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();
	}

	public String extractEmail(String token) {

		return getClaims(token).getSubject();
	}

	public String extractRole(String token) {

		return getClaims(token).get("role", String.class);
	}

	public String extractUserId(String token) {

		return getClaims(token).get("userId", String.class);
	}

	private Claims getClaims(String token) {

		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}
}