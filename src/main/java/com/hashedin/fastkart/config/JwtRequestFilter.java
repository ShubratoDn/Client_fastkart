package com.hashedin.fastkart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashedin.fastkart.exception.ErrorResponse;
import com.hashedin.fastkart.service.impl.CustomUserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		String header = request.getHeader("Authorization");
		String username = null ;
		String token = null;
		
		
		if(header != null && header.startsWith("Bearer")) {
			token = header.substring(7);
			
			
			try {
				 username = tokenProvider.getUsernameFromJWT(token);
				 log.info("User Found: {}", username);
					
			}catch (IllegalArgumentException e) {
				log.error("JWT token is invalid: {}", e.getMessage());
			}catch (ExpiredJwtException e) {
				 log.error("JWT token has expired: {}", e.getMessage());
				
		          // Create an ErrorResponse object
		            ErrorResponse errorResponse = new ErrorResponse(
		                new Date(),
		                HttpStatus.UNAUTHORIZED.toString(),
		                "Session has expired",
		                e.getMessage()
		            );

		            // Serialize the ErrorResponse object to JSON
		            ObjectMapper objectMapper = new ObjectMapper();
		            String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

		            // Set the HTTP status code and response content type
		            response.setStatus(HttpStatus.UNAUTHORIZED.value());
		            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		            // Write the JSON error response to the response body
		            response.getWriter().write(errorResponseJson);
		            return;
				
			}catch (MalformedJwtException e) {
				 log.error("JWT token is malformed: {}", e.getMessage());
			} catch (Exception e) {				
				log.error("An error occurred while processing JWT token: {}", e.getMessage());
		          // Create an ErrorResponse object
	            ErrorResponse errorResponse = new ErrorResponse(
	                new Date(),
	                HttpStatus.UNAUTHORIZED.toString(),
	                "Invalid Token",
	                e.getMessage()
	            );

	            // Serialize the ErrorResponse object to JSON
	            ObjectMapper objectMapper = new ObjectMapper();
	            String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

	            // Set the HTTP status code and response content type
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	            // Write the JSON error response to the response body
	            response.getWriter().write(errorResponseJson);
	            return;
			}			
		}else {
			log.debug("The token doesn't start with bearer.");
		}
		
		
		
		
		
		
		
		// VALIDATING THE TOKEN
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

		    UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);

		    if (tokenProvider.validateToken(token, userDetails)) {
		        log.info("Token validation successful for user: {}", username);

		        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		        log.info("User '{}' successfully authenticated.", username);
		    } else {
		        log.error("Failed validating the token for user: {}", username);
		    }
		} else {
		    log.warn("Username not found from the TOKEN");
		}
		
		
		
		filterChain.doFilter(request, response);
		
		
	}	
	
	

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// get JWT (token) from http request
//		String token = getJWTfromRequest(request);
//		// validate token
//		if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
//			// get email from token
//			String email = tokenProvider.getUsernameFromJWT(token);
//			// load user associated with token
//			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
//
//			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//					userDetails, null, userDetails.getAuthorities());
//			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//			// set spring security
//			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//		}
//		filterChain.doFilter(request, response);
//	}
//
//	// Bearer <accessToken>
//	private String getJWTfromRequest(HttpServletRequest request) {
//		String bearerToken = request.getHeader("Authorization");
//		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//			return bearerToken.substring(7, bearerToken.length());
//		}
//		return null;
//	}

}