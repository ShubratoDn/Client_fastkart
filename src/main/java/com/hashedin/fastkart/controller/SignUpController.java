package com.hashedin.fastkart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.form.SignUpForm;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.UsersRepository;

@Slf4j
//@Controller
public class SignUpController {

	@Autowired
	private UsersRepository usersRepository;

	private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@GetMapping("/buyerSignUp")
	public String buyerSignUp() {
		return "buyerSignUp";
	}

	@PostMapping("/buyerSignUp")
	public String buyerSignUp(@ModelAttribute(name = "signUpForm") SignUpForm signUpForm, Model model) {
		String username = signUpForm.getUsername();
		String password = signUpForm.getPassword();
		UserType type = UserType.valueOf(signUpForm.getType());

		Users users = usersRepository.findByUsername(username);

		if (users != null) {
			model.addAttribute("invalidCredentials", true);
			return "buyerSignUp";
		}
        log.info("saving buyer");
		Users user = new Users();
		user.setUsername(username);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		user.setUserType(type);
		usersRepository.save(user);
		return "login";
	}

	@GetMapping("/sellerSignUp")
	public String sellerSignUp() {
		return "sellerSignUp";
	}

	@PostMapping("/sellerSignUp")
	public String sellerSignUp(@ModelAttribute(name = "signUpForm") SignUpForm signUpForm, Model model) {
		String username = signUpForm.getUsername();
		String password = signUpForm.getPassword();
		UserType type = UserType.valueOf(signUpForm.getType());

		Users users = usersRepository.findByUsername(username);

		if (users != null) {
			model.addAttribute("invalidCredentials", true);
			return "sellerSignUp";
		}
        log.info("saving seller");
		Users user = new Users();
		user.setUsername(username);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		user.setUserType(type);
		usersRepository.save(user);
		return "login";
	}

}
