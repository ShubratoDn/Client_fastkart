package com.hashedin.fastkart.controller.JWT;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.fastkart.enums.ProductType;
import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.exception.ErrorResponse;
import com.hashedin.fastkart.form.ProductForm;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.service.ProductServices;
import com.hashedin.fastkart.service.UsersServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JwtProductController {

	@Autowired
	private ProductServices productServices;

    @Autowired
    private UsersServices usersServices;
    
    

    
    /**
     * Get a list of all products.
     *
     * @return A ResponseEntity with a list of all products.
     */
    @GetMapping("/ListProduct")
    public ResponseEntity<?> getAllProducts() {
        List<Products> allProducts = productServices.getAllProducts();
        log.info("Retrieved a list of all products.");
        return ResponseEntity.ok(allProducts);
    }
    
    
    
    
    /**
     * Get the products owned by the currently logged-in user.
     *
     * @return A ResponseEntity with a list of products owned by the user.
     */
    @GetMapping("/getmyproducts")
    public ResponseEntity<?> getMyProducts() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Users loggedUser = usersServices.getUserByUsername(name);
        List<Products> productsByUser = productServices.getProductsByUserId(loggedUser.getUserId());
        log.info("Retrieved products owned by user: {}", name);
        return ResponseEntity.ok(productsByUser);
    }
    
    
    

    /**
     * Get the products owned by a user based on their user ID.
     *
     * @param id The user ID for which products need to be retrieved.
     * @return A ResponseEntity with a list of products owned by the specified user.
     */
    @GetMapping("/Product/user/{id}")
    public ResponseEntity<?> getProductsByUserId(@PathVariable int id) {
        List<Products> productsByUser = productServices.getProductsByUserId(id);
        log.info("Retrieved products owned by user with ID: {}", id);
        return ResponseEntity.ok(productsByUser);
    }

    
    
    
    /**
     * Get a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return A ResponseEntity with the product if found, or an error response if not found.
     */
    @GetMapping("/Product/{id}")
    public ResponseEntity<?> getProductNew(@PathVariable("id") int id) {
        Products productById = productServices.getProductById(id);
        if (productById == null) {
            log.error("Product with ID {} not found.", id);
            ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.toString(),
                "Product not found",
                "The requested product was not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        log.info("Retrieved product with ID: {}", id);
        return ResponseEntity.ok(productById);
    }

    
    
    
    
    /**
     * Add a new product.
     *
     * @param productForm The product information to add.
     * @return A ResponseEntity indicating success or an error response if the product could not be added.
     */
    @PostMapping("/Product")
    public ResponseEntity<?> postProduct(@RequestBody ProductForm productForm) {
        String name = productForm.getName();
        String description = productForm.getDescription();
        Integer minBidAmount = productForm.getMinBidAmount();
        ProductType category = productForm.getCategory();
        Date currentDate = new Date();

        // Get the username of the logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users loggedUser = usersServices.getUserByUsername(username);

        log.info("Received request to add a product by user: {}", username);

        // Check if the logged-in user is a buyer
        if (loggedUser.getUserType() == UserType.BUYER) {
            log.error("Buyer '{}' tried to add a product. Buyers are not allowed to add products.", username);
            ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.FORBIDDEN.toString(),
                    "Buyer cannot add a product", "Buyer users are not allowed to add products");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Products product = new Products();
        product.setName(name);
        product.setDescription(description);
        product.setMinBidAmount(minBidAmount);
        product.setProductType(category);
        product.setCreationDateTime(currentDate);
        product.setUsers(loggedUser);

        if (productServices.addProduct(product) != null) {
            log.info("Product '{}' added successfully by user: {}", name, username);
            return ResponseEntity.ok("Product Added Successfully");
        } else {
            log.error("Failed to add product '{}' by user: {}", name, username);
            ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "Product not added", "There was an issue adding the product");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
