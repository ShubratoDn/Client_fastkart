package com.hashedin.fastkart.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.model.Products;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.impl.CustomUserDetailsServiceImpl;
import com.hashedin.fastkart.config.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsServiceImpl jwtUserDetailsService;

    private static HashMap<String, String> tokenMap = new HashMap<String, String>();

    public String loginLogic(String username, String password, Model model) {
        System.out.println("in loginService");
        Users users = usersRepository.findByUsername(username);
        if (users == null) {
            model.addAttribute("invalidUser", true);
            model.addAttribute("msg", "user doesn't exist");
            return "login";
        } else {
            if (!bCryptPasswordEncoder.matches(password, users.getPassword())) {
                model.addAttribute("invalidCredentials", true);
                model.addAttribute("msg", "username and password are incorrect");
                return "login";
            }
        }
        UserType type = users.getUserType();
        model.addAttribute("currentUser", users.getUserId());
        String token = "Bearer " + jwtTokenProvider.generateToken(jwtUserDetailsService.loadUserByUsername(username));
        setToken(username, token);
        model.addAttribute("token", token);
        if (type.equals(UserType.BUYER)) {
            List<Products> productsList = productsRepository.findAll();
            if (!productsList.isEmpty()) {
                model.addAttribute("productsList", productsList);
            } else {
                model.addAttribute("noProducts", true);
                model.addAttribute("msg", "No Product found");
            }
            model.addAttribute("buyerId", users.getUserId());
            log.info("Login Successful");
            return "buyerHome";
        } else {
            List<Products> productsList = productsRepository.findProductsByUserId(users.getUserId());
            if (!productsList.isEmpty()) {
                model.addAttribute("productsList", productsList);
            } else {
                model.addAttribute("noProducts", true);
                model.addAttribute("msg", "No Product found");
            }
            model.addAttribute("sellerId", users.getUserId());
            log.info("Login Successful");
            return "sellerHome";
        }
    }

    public void setToken(String username, String token) {
        tokenMap.put(username, token);
    }

    public String getToken(String username) {
        return tokenMap.get(username);
    }
}
