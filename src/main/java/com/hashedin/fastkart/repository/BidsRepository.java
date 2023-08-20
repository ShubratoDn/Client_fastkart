package com.hashedin.fastkart.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.model.Products;

@Repository
public interface BidsRepository extends JpaRepository<Bids,Integer> {

	List<Bids> findByProducts(Products products);
	
    @Query(value = "SELECT * FROM bids a WHERE a.product_id = ?1",nativeQuery = true)
    List<Bids> findAllBidsByProductId(Integer productId);

    @Query(value = "SELECT b.bid_amount, u.username FROM bids b JOIN users u ON b.user_id = u.user_id WHERE b.product_id = ?1",nativeQuery = true)
    List<Object[]> findBidAmountAndUserByProductId(Integer productId);

    @Query(value = "SELECT b.bid_amount, u.username FROM bids b JOIN users u WHERE b.user_id = u.user_id",nativeQuery = true)
    List<Object[]> findAllBidAmountAndUser();

    @Modifying
    @Query(value = "UPDATE bids SET bid_amount = ?2 AND creation_Date_time = ?3 WHERE bid_id = ?1", nativeQuery = true)
    void updateBidAmount(Long bidId, Double newBidAmount, Date date);

    @Query(value = "SELECT * FROM bids a WHERE a.product_id = ?1 and a.user_id= ?2", nativeQuery = true)
    Bids findBidsByProductIdAndUser(Integer productId, Integer userId);
}
