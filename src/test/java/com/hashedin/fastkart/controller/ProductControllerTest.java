package com.hashedin.fastkart.controller;

import com.hashedin.fastkart.enums.ProductType;
import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.form.ProductForm;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductsRepository productsRepository;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private BidsRepository bidsRepository;

	@Mock
	private LoginService loginService;

	@Mock
	private Model model;

	@Test
	public void testSellProduct() {
		String sellerId = "1";
		Integer id = Integer.parseInt(sellerId);

		String result = productController.sellProduct(sellerId, model);

		assertEquals("sellProduct", result);
		verify(model,times(1)).addAttribute("sellerId",id);
	}

	@Test
	public void testGetProductNewWithoutBids() {
		Users user = new Users();
		user.setUserId(1);
		user.setUserType(UserType.BUYER);
		Products product = new Products();
		product.setProductId(1);

		when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(productsRepository.findById(anyInt())).thenReturn(Optional.of(product));
		when(bidsRepository.findBidAmountAndUserByProductId(anyInt())).thenReturn(new ArrayList<>());

		String expectedResult = "productDetails";
		String actualResult = productController.getProductNew("1", 1, model);

		verify(model,never()).addAttribute("minBidAmountByBuyer",eq(anyInt()));
		verify(model,never()).addAttribute("maxBidAmountByBuyer",eq(anyInt()));
		verify(model,never()).addAttribute("hasPlacedBids",true);
		assertEquals(expectedResult,actualResult);
	}

	@Test
	public void testPostProduct() {
		ProductForm productForm = new ProductForm();
		productForm.setName("Test Product");
		productForm.setDescription("Test Description");
		productForm.setMinBidAmount(100);
		productForm.setCategory(ProductType.MOBILE);
		productForm.setSellerId("1");

		Users seller = new Users();
		seller.setUserId(1);
		when(usersRepository.findById(anyInt())).thenReturn(Optional.of(seller));

		String result = productController.postProduct(productForm, model);

		assertEquals("redirect:/seller/1", result);
		verify(productsRepository).save(any(Products.class));
	}
}