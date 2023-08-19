package com.hashedin.fastkart.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hashedin.fastkart.enums.ProductType;
import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.form.ProductForm;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.BidsRepository;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Controller
public class ProductController {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BidsRepository bidsRepository;

    @Autowired
    private LoginService loginService;

    @GetMapping("/ListProduct")
    public String sellProduct(@RequestParam(value = "sellerId", required = false) String sellerId, Model model) {
        log.info("product for seller ---> {}", sellerId);
        Integer id = Integer.parseInt(sellerId);
        model.addAttribute("sellerId", id);
        Optional<Users> user = usersRepository.findById(Integer.parseInt(sellerId));
        if (!user.isEmpty()) {
            model.addAttribute("token", loginService.getToken(user.get().getUsername()));
        }
        return "sellProduct";
    }

    @GetMapping("/Product/{id}")
    public String getProductNew(@PathVariable("id") String id, @RequestParam("userId") Integer userId, Model model) {

        Optional<Users> user = usersRepository.findById(userId);
        Optional<Products> product = productsRepository.findById(Integer.parseInt(id));
        List<Object[]> bidsExtraVar = bidsRepository.findBidAmountAndUserByProductId(product.get().getProductId());
        if (!bidsExtraVar.isEmpty())
            model.addAttribute("hasPlacedBids", true);
        if (product.isPresent())
            model.addAttribute("productData", product);
        else {
        }
        if (user.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("token", loginService.getToken(user.get().getUsername()));
            if (user.get().getUserType().equals(UserType.BUYER)) {
                List<Object[]> results = bidsRepository.findBidAmountAndUserByProductId(product.get().getProductId());
                log.info("buyer bid count ---> {} ", results.size());
                if (!results.isEmpty()) {
                    model.addAttribute("bidData", results);
                } else {
                    model.addAttribute("noBids", true);
                }
            } else {
                Integer minBidAmountByBuyer = Integer.MAX_VALUE;
                Integer maxBidAmountByBuyer = Integer.MIN_VALUE;

                List<Object[]> results = bidsRepository.findBidAmountAndUserByProductId(Integer.parseInt(id));
                log.info("product bid count ---> {} ", results.size());
                model.addAttribute("productData", product);
                model.addAttribute("sellerId", product.get().getUsers().getUserId());
                model.addAttribute("user", user);
                if (!results.isEmpty()) {
                    model.addAttribute("bidData", results);
                    for (Object[] result : results) {
                        Integer amount = (Integer) result[0];
                        minBidAmountByBuyer = Math.min(minBidAmountByBuyer, amount);
                        maxBidAmountByBuyer = Math.max(maxBidAmountByBuyer, amount);
                    }
                    model.addAttribute("minBidAmountByBuyer", minBidAmountByBuyer);
                    model.addAttribute("maxBidAmountByBuyer", maxBidAmountByBuyer);
                } else {
                    model.addAttribute("noBids", true);
                }
            }
        } else {
        }
        return "productDetails";
    }

    @PostMapping("/Product")
    public String postProduct(@ModelAttribute(name = "productForm") ProductForm productForm, Model model) {
        String name = productForm.getName();
        String description = productForm.getDescription();
        Integer minBidAmount = productForm.getMinBidAmount();
        ProductType category = productForm.getCategory();
        Integer sellerId = Integer.parseInt(productForm.getSellerId());
        Date currentDate = new Date();
        Optional<Users> seller = usersRepository.findById(sellerId);
        log.info("{}", productForm);
        if (seller.isPresent()) {
            Products product = new Products();
            product.setName(name);
            product.setDescription(description);
            product.setMinBidAmount(minBidAmount);
            product.setProductType(category);
            product.setCreationDateTime(currentDate);
            product.setUsers(seller.get());
            productsRepository.save(product);
            model.addAttribute("token", loginService.getToken(seller.get().getUsername()));
            model.addAttribute("user", seller);
        }
        return "redirect:/seller/" + productForm.getSellerId();
    }
}
