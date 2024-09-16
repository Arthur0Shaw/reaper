package com.money.reaper.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

	private static final String SECRET_KEY = "PJC7HnliwcxXw4FM8Ep3sX9NIL3R5CZnDvp8IyyCSlg=";
	private static final Set<String> invalidatedTokens = new HashSet<>();

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> extractClaims = new HashMap<>();
		extractClaims.put("tokenId", UUID.randomUUID().toString());
		return generate(extractClaims, userDetails);
	}

	public String generate(Map<String, Object> extractClaims, UserDetails userDetail) {
		return Jwts.builder().setClaims(extractClaims).setSubject(userDetail.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractsAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractsAllClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
		} catch (JwtException e) {
			throw new RuntimeException("Invalid token: " + e.getMessage());
		}
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isTokenValid(String token, UserDetails userDetail) {
		final String username = extractUserName(token);
		return (username.equals(userDetail.getUsername())) && !isTokenExpired(token) && !isTokenInvalidated(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public void invalidateToken(String token) {
		invalidatedTokens.add(token);
	}

	private boolean isTokenInvalidated(String token) {
		return invalidatedTokens.contains(token);
	}

}
