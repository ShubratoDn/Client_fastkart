package com.hashedin.fastkart.service;

import org.springframework.stereotype.Service;

import com.hashedin.fastkart.model.Users;

@Service
public interface UsersServices {

	public Users userRegister(Users user);
	
	public Users getUserByUsername(String username);
	
}