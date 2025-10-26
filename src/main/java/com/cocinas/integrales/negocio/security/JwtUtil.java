package com.cocinas.integrales.negocio.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);

	private byte[] secretBytes = Base64.getEncoder().encode("claveSuperSeguraDeAlMenos64Caracteres1234567890abcdef!@#$%".getBytes());
	private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
	private Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());


	public String generateToken(String username) {
		  return Jwts.builder()
			        .setSubject(username)
			        .setIssuedAt(new Date())
			        .setExpiration(new Date(System.currentTimeMillis() + 86400000))
			        .signWith(SignatureAlgorithm.HS512, signingKey)
			        .compact();
	}

	public boolean validateToken(String token, String username) {
		final String usernameFromToken = extractUsername(token);
		return (usernameFromToken.equals(username) && !isTokenExpired(token));
	}

	// Extract the username from the token
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	// Extract all claims from the token
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretBytes).parseClaimsJws(token).getBody();
	}

	// Check if the token has expired
	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}
}












