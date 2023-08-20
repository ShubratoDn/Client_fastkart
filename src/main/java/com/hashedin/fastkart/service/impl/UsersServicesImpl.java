package com.hashedin.fastkart.service.impl;

import org.modelmapper.ModelMapper;
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
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UsersRepository usersRepository;
	
	/**
     * Register a new user.
     *
     * @param user The user to be registered.
     * @return The registered user.
     */
    @Override
    public Users userRegister(Users user) {
        // Encode the user's password before saving it to the database
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Users savedUser = usersRepository.save(user);
        return savedUser;
    }

    /**
     * Get a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username, or null if not found.
     */
    @Override
    public Users getUserByUsername(String username) {
        Users user = usersRepository.findByUsername(username);
        return user;
    }
	
}