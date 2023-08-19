package com.hashedin.fastkart.controller;

import com.hashedin.fastkart.form.BidForm;
import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidControllerTest {

	@InjectMocks
	private BidController bidController;

	@Mock
	private BidsRepository bidsRepository;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private ProductsRepository productsRepository;

	@Mock
	private LoginService loginService;

	@Mock
	private Model model;

	@Test
	public void testPostBid() {
		Users user = new Users();
		user.setUserId(1);
		Products product = new Products();
		Bids bid = new Bids();

		when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(productsRepository.findById(anyInt())).thenReturn(Optional.of(product));
		when(bidsRepository.findBidsByProductIdAndUser(anyInt(),anyInt())).thenReturn(null);
		when(bidsRepository.save(any(Bids.class))).thenReturn(bid);

		BidForm bidForm = new BidForm();
		bidForm.setSellerId("1");
		bidForm.setProductId("1");
		bidForm.setBidAmount("100");
		String result = bidController.postBid(bidForm, model);

		verify(bidsRepository,times(1)).save(any(Bids.class));
		assertEquals("redirect:/buyer/" + user.getUserId(), result);
	}
}