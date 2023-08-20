package com.hashedin.fastkart.service;

import java.util.List;

import com.hashedin.fastkart.model.Bids;


public interface BidServices {
	public Bids addBid(Bids bid);	
	public List<Bids> getAllBidsByProductId(int id);
}
