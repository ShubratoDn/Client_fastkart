package com.hashedin.fastkart.controller.JWT;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hashedin.fastkart.service.BidServices;
import com.hashedin.fastkart.service.LoginService;
import com.hashedin.fastkart.service.ProductServices;
import com.hashedin.fastkart.service.UsersServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.exception.ErrorResponse;
import com.hashedin.fastkart.form.BidForm;
import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;

@Slf4j
@RestController
public class JwtBidController {

	@Autowired	
	private ProductServices productServices;

    @Autowired
    private UsersServices usersServices;
    
    @Autowired
    private BidServices bidServices;

    @PostMapping("/Bid")    
    public ResponseEntity<?> postBid(@RequestBody BidForm bidForm) {
        
    	
        Integer bidAmount = bidForm.getBidAmount();
        Date currentDate = new Date();
        
                
    	
        // Get the username of the logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users loggedUser = usersServices.getUserByUsername(username);
        
        // Check if the logged-in user is a seller
        if (loggedUser.getUserType() == UserType.SELLER) {            
            ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.FORBIDDEN.toString(),
                    "Seller cannot add a Bid", "Seller users are not allowed to add Bids");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        

        if(bidForm.getProductId() == null) {
        	ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(),
					"Product not found", "Product can not be null");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        
        //getting the product
		Products product = productServices.getProductById(Integer.parseInt(bidForm.getProductId()));
		if (product == null) {
			ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(),
					"Product not found", "The product with ID " + bidForm.getProductId() + " was not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		
		//bid amount configuration
		if(bidAmount < product.getMinBidAmount()) {
			ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST.toString(),
					"Bid amount too low",
					"The bid amount must be greater than the minimum bid amount that is "+product.getMinBidAmount()+" for this product.");
			return ResponseEntity.badRequest().body(errorResponse);
		}
		
		
		
		Bids bid = new Bids();
		bid.setBidAmount(bidAmount);
		bid.setCreationDateTime(currentDate);
		bid.setProducts(product);
		bid.setUsers(loggedUser);
		
		
		Bids addBid = bidServices.addBid(bid);

		if (addBid != null) {
			return ResponseEntity.ok("Added Bid");
		} else {
			ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
					"Bid not added", "The bid could not be added. Please try again later.");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
    }
    
    
    
    
    
    @GetMapping("/getBids/product/{id}")
    public ResponseEntity<?> getProductBids(@PathVariable int id){
    	
    	List<Bids> allBidsByProductId = bidServices.getAllBidsByProductId(id);
    	
    	return ResponseEntity.ok(allBidsByProductId);
    }
    
    
    
    
    
    
    

}