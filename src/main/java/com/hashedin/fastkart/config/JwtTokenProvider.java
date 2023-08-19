package com.hashedin.fastkart.config;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

//	@Value("${app.jwt-secret}")
	private String jwtSecret = "user";

//	@Value("${app.jwt-expiration-milliseconds}")
	private int jwtExpirationInMs = 60000;

	// generate token
	public String generateToken(UserDetails userDetails) {
		String username = userDetails.getUsername();
		Date expireDate = new Date(new Date().getTime() + jwtExpirationInMs);

		return Jwts.builder().setClaims(new HashMap<>()).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	// get username from the token
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	// validate JWT token
//	public boolean validateToken(String token) {
//		try {
//			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//			return true;
//		} catch (SignatureException ex) {
//			throw new JWTAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
//		} catch (MalformedJwtException ex) {
//			throw new JWTAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
//		} catch (ExpiredJwtException ex) {
//			throw new JWTAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
//		} catch (UnsupportedJwtException ex) {
//			throw new JWTAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
//		} catch (IllegalArgumentException ex) {
//			throw new JWTAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
//		}
//	}	
	
	
	
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromJWT(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	 
	 
	 
	
	
	
	
	public String refreshToken(String token) {
	    final Date expirationDate = new Date();

	    final Claims claims = getAllClaimsFromToken(token);
	    claims.setExpiration(expirationDate);

	    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}
}