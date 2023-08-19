package com.hashedin.fastkart.service;

import com.hashedin.fastkart.model.Products;

public interface ProductServices {

	public Products addProduct(Products products);

	public Products getProductById(int id);
	
}
