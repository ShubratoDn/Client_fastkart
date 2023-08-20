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
	
	@Override
	public Products addProduct(Products products) {
		Products save = productsRepository.save(products);
		return save;
	}
	

	
	//get all products
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



	@Override
	public List<Products> getAllProducts() {
		List<Products> findAll = productsRepository.findAll();
		return findAll;
	}

	
	
	public List<Products> getProductsByUserId(Integer id){
		List<Products> productsList = productsRepository.findProductsByUserId(id);		
		return productsList;
	}
	
	
}
