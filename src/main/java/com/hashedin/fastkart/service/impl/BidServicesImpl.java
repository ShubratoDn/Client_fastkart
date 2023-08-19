package com.hashedin.fastkart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.service.BidServices;

@Service
public class BidServicesImpl implements BidServices {

	@Autowired
	private BidsRepository bidsRepository;
	
	@Override
	public Bids addBid(Bids bid) {
		Bids save = bidsRepository.save(bid);
		return save;
	}

}
