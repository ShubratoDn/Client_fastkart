package com.hashedin.fastkart.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import com.hashedin.fastkart.controller.SignUpController;
import com.hashedin.fastkart.form.SignUpForm;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
public class SignUpControllerTest {

	@InjectMocks
	private SignUpController signUpController;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private Model model;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuyerSignUpWithValidData() {
		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setUsername("buyerUser");
		signUpForm.setPassword("password");
		signUpForm.setType("BUYER");

		Users users = new Users();
		when(usersRepository.findByUsername(anyString())).thenReturn(users);

		String result = signUpController.buyerSignUp(signUpForm, model);

		assertEquals("login", result);
		verify(usersRepository).save(any(Users.class));
		verify(model, never()).addAttribute(eq("invalidCredentials"), anyBoolean());
	}

	@Test
	public void testSellerSignUpWithExistingUser() {
		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setUsername("existingUser");
		signUpForm.setPassword("password");
		signUpForm.setType("SELLER");

		when(usersRepository.findByUsername(anyString())).thenReturn(null);

		String result = signUpController.sellerSignUp(signUpForm, model);

		assertEquals("sellerSignUp", result);
		verify(model).addAttribute(eq("invalidCredentials"), eq(true));
		verify(usersRepository, never()).save(any(Users.class));
	}
}
