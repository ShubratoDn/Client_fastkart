package com.hashedin.fastkart.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

	@InjectMocks
	private CustomErrorController customErrorController;

	@Mock
	private Model model;

	@Test
	public void testHandleError() {
		String expectedViewName = "error";
		String expectedErrorMessage = "An error occurred. Please try again later.";

		String result = customErrorController.handleError(model);

		assertEquals(expectedViewName, result);
		verify(model,times(1)).addAttribute("errorMessage", expectedErrorMessage);
	}
}
