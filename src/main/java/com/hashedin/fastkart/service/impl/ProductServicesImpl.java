package com.hashedin.fastkart.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.service.ProductServices;

@Service
public class ProductServicesImpl implements ProductServices {

	@Autowired
	private ProductsRepository productsRepository;
	
	@Autowired
	private BidsRepository bidsRepository;
	
	@Autowired
	private ModelMapper mapper;
	

    /**
     * Add a new product.
     *
     * @param product The product to be added.
     * @return The added product.
     */
	@Override
	public Products addProduct(Products products) {
		Products save = productsRepository.save(products);
		return save;
	}
	

	
	/**
     * Get a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID, or null if not found.
     */
	@Override
	public Products getProductById(int id) {
		Products product = null;
				
		Optional<Products> findById = productsRepository.findById(id);
		if(findById != null) {
			product = findById.orElse(null);
		}			
		if(product == null) {
			return null;
		}
		
		//get bids for this product
		List<Bids> allBids = bidsRepository.findAllBidsByProductId(product.getProductId());
		
		product.setBidsList(allBids);
		
		return product;
	}



	/**
     * Get a list of all products.
     *
     * @return A list of all products.
     */
	@Override
	public List<Products> getAllProducts() {
		List<Products> findAll = productsRepository.findAll();
		return findAll;
	}

	
	/**
     * Get a list of products by user ID.
     *
     * @param id The ID of the user.
     * @return A list of products associated with the user.
     */
	public List<Products> getProductsByUserId(Integer id){
		List<Products> productsList = productsRepository.findProductsByUserId(id);		
		return productsList;
	}
	
	
}
