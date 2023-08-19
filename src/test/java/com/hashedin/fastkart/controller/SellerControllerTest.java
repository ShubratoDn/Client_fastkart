package com.hashedin.fastkart.controller;

import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerControllerTest {

	@InjectMocks
	private SellerController sellerController;

	@Mock
	private ProductsRepository productsRepository;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private LoginService loginService;

	@Mock
	private Model model;

	@Test
	public void testGetSellerPageWithProducts() {
		List<Products> productsList = new ArrayList<>();
		productsList.add(new Products());
		productsList.add(new Products());

		loginService.setToken("test", "test");
		when(productsRepository.findProductsByUserId(anyInt())).thenReturn(productsList);
		Users user = new Users();
		user.setUserId(1);
		user.setUsername("test");
		when(usersRepository.findById(1)).thenReturn(Optional.of(user));
		when(loginService.getToken("test")).thenReturn("test");

		String result = sellerController.getSellerPage("1", model);

		assertEquals("sellerHome", result);
		verify(model).addAttribute(eq("productsList"), eq(productsList));
		verify(model).addAttribute(eq("sellerId"), eq("1"));
		verify(model, never()).addAttribute(eq("noProducts"), anyBoolean());
	}

	@Test
	public void testGetSellerPageWithNoProducts() {
		List<Products> emptyProductsList = new ArrayList<>();

		when(productsRepository.findProductsByUserId(anyInt())).thenReturn(emptyProductsList);
		Users user = new Users();
		user.setUserId(2);
		user.setUsername("test");
		when(usersRepository.findById(2)).thenReturn(Optional.of(user));
		when(loginService.getToken("test")).thenReturn("test");

		String result = sellerController.getSellerPage("2", model);

		assertEquals("sellerHome", result);
		verify(model).addAttribute(eq("sellerId"), eq("2"));
		verify(model).addAttribute(eq("noProducts"), eq(true));
		verify(model, never()).addAttribute(eq("productsList"), any());
	}
}
