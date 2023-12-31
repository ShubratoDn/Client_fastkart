package com.hashedin.fastkart.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.service.BidServices;

@Service
public class BidServicesImpl implements BidServices {

	@Autowired
	private BidsRepository bidsRepository;
	
	/**
     * Add a new bid to the database.
     *
     * @param bid The bid to be added.
     * @return The added bid.
     */
    @Override
    public Bids addBid(Bids bid) {
        Bids save = bidsRepository.save(bid);
        return save;
    }
    
    
    

    /**
     * Get all bids associated with a specific product.
     *
     * @param id The ID of the product for which bids are to be retrieved.
     * @return A list of bids associated with the specified product.
     */
    @Override
    public List<Bids> getAllBidsByProductId(int id) {
        Products products = new Products();
        products.setProductId(id);

        List<Bids> findByProducts = bidsRepository.findByProducts(products);
        return findByProducts;
    }	
}
