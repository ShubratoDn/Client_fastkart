package com.hashedin.fastkart.controller;

import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuyerControllerTest {


	@InjectMocks
	private BuyerController buyerController;

	@Mock
	private ProductsRepository productsRepository;

	@Mock
	private Model model;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private LoginService loginService;

	@Test
	public void testGetSellerPage() {
		List<Products> productsList = new ArrayList<>();
		productsList.add(new Products());
		productsList.add(new Products());

		when(productsRepository.findAll()).thenReturn(productsList);
		Users user = new Users();
		user.setUserId(1);
		user.setUsername("test");
		when(usersRepository.findById(1)).thenReturn(Optional.of(user));
		when(loginService.getToken("test")).thenReturn("test");
		String result = buyerController.getSellerPage("1", model);

		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Object> productListCaptor = ArgumentCaptor.forClass(Object.class);
		ArgumentCaptor<Object> noProductsCaptor = ArgumentCaptor.forClass(Object.class);

		verify(model).addAttribute(eq("buyerId"), idCaptor.capture());
		verify(model).addAttribute(eq("productsList"), productListCaptor.capture());
		verify(model, never()).addAttribute(eq("noProducts"), anyBoolean());

		assertEquals("1", idCaptor.getValue());
		assertEquals(productsList, productListCaptor.getValue());
		assertEquals("buyerHome", result);
	}

	@Test
	public void testGetSellerPageWithNoProducts() {
		List<Products> emptyProductsList = new ArrayList<>();

		when(productsRepository.findAll()).thenReturn(emptyProductsList);
		Users user = new Users();
		user.setUserId(1);
		user.setUsername("test");
		when(usersRepository.findById(1)).thenReturn(Optional.of(user));
		when(loginService.getToken("test")).thenReturn("test");

		String result = buyerController.getSellerPage("1", model);

		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Object> noProductsCaptor = ArgumentCaptor.forClass(Object.class);

		verify(model).addAttribute(eq("buyerId"), idCaptor.capture());
		verify(model, never()).addAttribute(eq("productsList"), any());
		verify(model).addAttribute(eq("noProducts"), noProductsCaptor.capture());

		assertEquals("1", idCaptor.getValue());
		assertTrue((Boolean) noProductsCaptor.getValue());
		assertEquals("buyerHome", result);
	}
}