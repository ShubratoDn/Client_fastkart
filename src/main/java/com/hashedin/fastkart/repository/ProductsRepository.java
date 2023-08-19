package com.hashedin.fastkart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hashedin.fastkart.model.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

    @Query(value = "SELECT * FROM products a WHERE a.user_id = ?1", nativeQuery = true)
    List<Products> findProductsByUserId(Integer userId);
}
