package com.hashedin.fastkart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hashedin.fastkart.repository.UsersRepository;
import com.hashedin.fastkart.service.impl.CustomUserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.hashedin.fastkart.enums.UserType;
import com.hashedin.fastkart.model.Users;
import com.hashedin.fastkart.repository.ProductsRepository;
import com.hashedin.fastkart.config.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserDetailsServiceImpl jwtUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Model model;

    @Test
    public void testLoginLogicWithInvalidUser() {
        when(usersRepository.findByUsername(anyString())).thenReturn(null);

        String result = loginService.loginLogic("nonExistentUser", "password", model);

        assertEquals("login", result);
        verify(model).addAttribute(eq("invalidUser"), eq(true));
        verify(model, never()).addAttribute(eq("invalidCredentials"), anyBoolean());
    }

    @Test
    public void testLoginLogicWithInvalidCredentials() {
        Users user = new Users();
        user.setPassword("correctPassword");

        when(usersRepository.findByUsername(anyString())).thenReturn(user);

        String result = loginService.loginLogic("existingUser", "incorrectPassword", model);

        assertEquals("login", result);
        verify(model).addAttribute(eq("invalidCredentials"), eq(true));
        verify(model, never()).addAttribute(eq("invalidUser"), anyBoolean());
    }

    @Test
    public void testLoginLogicValidUser() {
        Users user = new Users();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setPassword("$2a$10$sGI3WPl.X4gJW2jjB0jHYeLnVqfC5pH/A9iGWp/n9jTPpKKuhEogK");
        user.setUserType(UserType.BUYER);

        when(usersRepository.findByUsername("testuser")).thenReturn(user);
        when(jwtUserDetailsService.loadUserByUsername("testuser")).thenReturn(null); // Mock UserDetails

        Model model = new ExtendedModelMap();
        String result = loginService.loginLogic("testuser", "Password@1234", model);

        // Assertions and verifications
        assertEquals(result, "buyerHome");
        assertEquals(model.containsAttribute("currentUser"), true);
        assertEquals(model.containsAttribute("token"), true);
        assertEquals(model.containsAttribute("productsList"), false);
        assertEquals(model.containsAttribute("noProducts"), true);
    }

    @Test
    public void testLoginLogicInvalidUser() {
        when(usersRepository.findByUsername("unknownuser")).thenReturn(null);

        Model model = new ExtendedModelMap();
        String result = loginService.loginLogic("unknownuser", "password", model);

        assertEquals(result, "login");
        assertEquals(model.containsAttribute("invalidUser"), true);
        verify(productsRepository, never()).findAll();
    }

}
