package com.hashedin.fastkart.controller;

import com.hashedin.fastkart.form.LoginForm;
import com.hashedin.fastkart.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

	@InjectMocks
	private LoginController loginController;

	@Mock
	private LoginService loginService;

	@Mock
	private Model model;

	@Test
	public void testGetLoginForm() {
		String viewName = loginController.getLoginForm();
		assertEquals("login", viewName);
	}

	@Test
	public void testLogin() {
		LoginForm loginForm = new LoginForm();
		loginForm.setUsername("username");
		loginForm.setPassword("password");

		String expectedResult = "homePage";

		when(loginService.loginLogic(anyString(), anyString(), any(Model.class))).thenReturn(expectedResult);

		String result = loginController.login(loginForm, model);

		assertEquals(expectedResult, result);
		verify(loginService,times(1)).loginLogic("username","password",model);
	}
}
