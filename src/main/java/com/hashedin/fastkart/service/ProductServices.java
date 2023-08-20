package com.hashedin.fastkart.service;

import java.util.List;

import com.hashedin.fastkart.model.Products;


public interface ProductServices {

	public Products addProduct(Products products);

	public Products getProductById(int id);
	
	public java.util.List<Products> getAllProducts();
	
	public List<Products> getProductsByUserId(Integer id);
}
