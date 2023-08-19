package com.hashedin.fastkart.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashedin.fastkart.exception.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// Create an ErrorResponse object
		ErrorResponse errorResponse = new ErrorResponse(new Date(), "Unauthorized", "Access denied",
				authException.getMessage());

		// Serialize the ErrorResponse object to JSON
		ObjectMapper objectMapper = new ObjectMapper();
		String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

		// Write the JSON response to the output stream
		try (PrintWriter out = response.getWriter()) {
			out.print(errorResponseJson);
		}

	}
}