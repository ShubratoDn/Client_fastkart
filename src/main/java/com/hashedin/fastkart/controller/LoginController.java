package com.hashedin.fastkart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hashedin.fastkart.form.LoginForm;
import com.hashedin.fastkart.service.LoginService;

//@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping(value = { "/login", "/" })
    public String getLoginForm() {
        return "login";
    }

    @PostMapping("/signin")
    public String login(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        System.out.println("in login");
        return loginService.loginLogic(username, password, model);
    }
}
