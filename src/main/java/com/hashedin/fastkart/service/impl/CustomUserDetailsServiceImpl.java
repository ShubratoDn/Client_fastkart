package com.hashedin.fastkart.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hashedin.fastkart.config.CustomUserDetails;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.UsersRepository;

/**
 * Implementation of the Spring Security UserDetailsService interface.
 * This class is responsible for loading user details for authentication.
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    
    /**
     * Load user details by username.
     *
     * @param userName The username of the user to load.
     * @return UserDetails containing user information.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(userName);        
        if(user == null) {
			throw new UsernameNotFoundException("User name not found");
		}
        
		CustomUserDetails customUserDetails = new CustomUserDetails(user);		
		return customUserDetails;        
    }

}