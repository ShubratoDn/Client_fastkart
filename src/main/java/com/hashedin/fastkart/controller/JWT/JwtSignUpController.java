package com.hashedin.fastkart.controller.JWT;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.exception.ErrorResponse;
import com.hashedin.fastkart.form.SignUpForm;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.service.UsersServices;

@Slf4j
@RestController
public class JwtSignUpController {



	@Autowired
	private UsersServices usersservices;
	

    /**
     * Register a new buyer.
     *
     * @param signUpForm The sign-up form containing buyer details.
     * @return ResponseEntity containing a success message or error response.
     */	
	@PostMapping("/buyerSignUp")
	public ResponseEntity<?> buyerSignUp(@RequestBody SignUpForm signUpForm) {
		
		log.info("Received request to register a buyer with username: {}", signUpForm.getUsername());
        
		
		String username = signUpForm.getUsername();
		String password = signUpForm.getPassword();
//		UserType type = UserType.valueOf(signUpForm.getType());
		
		log.debug("Validating input for buyer registration...");
		if (username == null || username.isBlank() || password == null || password.isBlank() ) {
			log.error("Invalid input for buyer registration.");
		    ErrorResponse errorResponse = new ErrorResponse(
		        new Date(),
		        HttpStatus.BAD_REQUEST.toString(),
		        "Invalid Input",
		        "Username or password cannot be blank or null. Please provide a valid username."
		    );

		    return ResponseEntity.badRequest().body(errorResponse);
		}

		log.info("Checking if the username already exists...");
		Users existingUser = usersservices.getUserByUsername(username);
		if(existingUser != null) {
			log.error("Buyer registration failed. Username '{}' already exists.", username);
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST.toString(), "Buyer registration failed", "The provided username already exists. Please choose a different username."));
		}

		Users users = new Users();
		users.setUserType(UserType.BUYER);
		users.setUsername(username);		
		users.setPassword(password);
		
		log.debug("Registering the buyer user...");
		usersservices.userRegister(users);		
		log.info("Buyer registered successfully with username: {}", username);         
		return new ResponseEntity<>("Buyer registered Successfull!", HttpStatus.CREATED);
	}

	
	
	/**
	 * Register a new seller.
	 *
	 * @param signUpForm The sign-up form containing seller details.
	 * @return ResponseEntity containing a success message or error response.
	 */
	@PostMapping("/sellerSignUp")
	public ResponseEntity<?> sellerSignUp(@RequestBody SignUpForm signUpForm) {
		log.info("Received request to register a seller with username: {}", signUpForm.getUsername());
        
		String username = signUpForm.getUsername();
		String password = signUpForm.getPassword();
		
		log.info("Validating input for seller registration...");        
		if (username == null || username.isBlank() || password == null || password.isBlank() ) {
			log.error("Invalid input for seller registration.");
		    ErrorResponse errorResponse = new ErrorResponse(
		        new Date(),
		        HttpStatus.BAD_REQUEST.toString(),
		        "Invalid Input",
		        "Username or password cannot be blank or null. Please provide a valid username."
		    );
		    return ResponseEntity.badRequest().body(errorResponse);
		}
		
		
		Users existingUser = usersservices.getUserByUsername(username);
		if(existingUser != null) {
			log.error("Seller registration failed. Username '{}' already exists.", username);
			return ResponseEntity.badRequest().body(new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST.toString(), "Seller registration failed", "The provided username already exists. Please choose a different username."));
		}

		Users users = new Users();
		users.setUserType(UserType.SELLER);
		users.setUsername(username);		
		users.setPassword(password);
		
		log.debug("Registering the seller user...");
		usersservices.userRegister(users);		
		
		log.info("Seller registered successfully with username: {}", username);
		return new ResponseEntity<>("Seller Registered Successfull", HttpStatus.CREATED);
	}

}