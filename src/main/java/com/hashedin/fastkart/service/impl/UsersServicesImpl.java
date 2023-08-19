package com.hashedin.fastkart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.UsersServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsersServicesImpl  implements UsersServices{

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public Users userRegister(Users user) {		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));		
		Users save = usersRepository.save(user);		
		return save;
	}

	@Override
	public Users getUserByUsername(String username) {
		Users users = usersRepository.findByUsername(username);
		return users;
	}

}