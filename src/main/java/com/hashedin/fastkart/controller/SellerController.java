package com.hashedin.fastkart.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Controller
public class SellerController {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LoginService loginService;

    @GetMapping("/seller/{id}")
    public String getSellerPage(@PathVariable("id") String id, Model model) {
        List<Products> productsList = productsRepository.findProductsByUserId(Integer.parseInt(id));
        log.info(" seller home ---> {} ", id);
        Optional<Users> user = usersRepository.findById(Integer.parseInt(id));
        if (!productsList.isEmpty()) {
            model.addAttribute("productsList", productsList);
        } else {
            model.addAttribute("noProducts", true);
        }
        model.addAttribute("token", loginService.getToken(user.get().getUsername()));
        model.addAttribute("sellerId", id);
        return "sellerHome";
    }
}
