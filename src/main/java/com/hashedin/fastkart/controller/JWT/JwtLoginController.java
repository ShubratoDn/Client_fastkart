package com.hashedin.fastkart.controller.JWT;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.hashedin.fastkart.config.JwtTokenProvider;
import com.hashedin.fastkart.exception.ErrorResponse;
import com.hashedin.fastkart.form.LoginForm;
import com.hashedin.fastkart.form.LoginResponse;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.service.UsersServices;
import com.hashedin.fastkart.service.impl.CustomUserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class JwtLoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UsersServices services;

	
	
	/**
     * A test endpoint to check if the application is running properly.
     *
     * @return A ResponseEntity with an "ok" message.
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        log.info("Test endpoint accessed. Application is running properly.");
        return ResponseEntity.ok("ok");
    }

    
    
    /**
     * Handles the HTTP POST request for user login.
     *
     * @param loginForm The login form containing the username and password.
     * @return A ResponseEntity with a login response or an error response.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        log.info("Received login request for username: {}", username);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.error("Account is disabled for username: {}", username);
            // Return a response for a disabled account
            return new ResponseEntity<>(new ErrorResponse(new Date(), HttpStatus.FORBIDDEN.toString(), "Account is disabled", "The account associated with this username is disabled."), HttpStatus.FORBIDDEN);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for username: {}", username);
            // Return a response for incorrect password
            return new ResponseEntity<>(new ErrorResponse(new Date(), HttpStatus.UNAUTHORIZED.toString(), "Invalid credentials", "The provided username or password is incorrect."), HttpStatus.UNAUTHORIZED);
        } catch (InternalAuthenticationServiceException e) {
            log.error("Internal authentication error for username: {}", username);
            // Return a response for internal authentication service error
            return new ResponseEntity<>(new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Authentication Error", "An internal authentication error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
        String token = jwtTokenProvider.generateToken(userDetails);

        Users user = services.getUserByUsername(username);
        LoginResponse loginResponse = new LoginResponse(token, user);

        log.info("User '{}' logged in successfully.", username);

        return ResponseEntity.ok(loginResponse);
    }
        
}
