package com.hashedin.fastkart.controller;

import java.util.Date;
import java.util.Optional;

import com.hashedin.fastkart.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hashedin.fastkart.form.BidForm;
import com.hashedin.fastkart.model.Bids;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;

@Slf4j
//@Controller
public class BidController {

    @Autowired
    private BidsRepository bidsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private LoginService loginService;

    @PostMapping("/Bid")
    public String postBid(@ModelAttribute(name = "bidForm") BidForm bidForm, Model model) {
        Integer bidAmount = Integer.parseInt(bidForm.getBidAmount());
        Date currentDate = new Date();
        Optional<Users> user = usersRepository.findById(Integer.parseInt(bidForm.getSellerId()));
        Optional<Products> product = productsRepository.findById(Integer.parseInt(bidForm.getProductId()));
        log.info("productid to find ---->{}", bidForm.getProductId());
        if (user.isPresent() && product.isPresent()) {
            Bids bid = bidsRepository.findBidsByProductIdAndUser(Integer.parseInt(bidForm.getProductId()),
                    user.get().getUserId());
            if (bid == null) {
                bid = new Bids();
                bid.setUsers(user.get());
                bid.setProducts(product.get());
            }
            bid.setBidAmount(bidAmount);
            bid.setCreationDateTime(currentDate);
            bidsRepository.save(bid);
            model.addAttribute("token", loginService.getToken(user.get().getUsername()));
        }

        return "redirect:/buyer/" + user.get().getUserId();
    }

}