package com.hashedin.fastkart.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hashedin.fastkart.service.impl.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Bean
	JwtRequestFilter jwtRequestFilter() {
		return new JwtRequestFilter();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

//		httpSecurity.csrf().disable().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//				.authorizeRequests((authorize) -> authorize
//						.antMatchers("/login", "/", "/buyerSignUp", "/sellerSignUp", "/signin", "/ListProduct/**",
//								"/Product/**", "/seller/**", "/Bid", "/buyer/**")
//						.permitAll().antMatchers("/css/**", "/images/**", "/lib/**","/js/**").permitAll()
//						.antMatchers("/buyerHome", "/productDetails", "/sellerHome", "/sellProduct").permitAll()
//						.anyRequest().authenticated());
//		httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
//		return httpSecurity.build();
		

		httpSecurity.cors().and().csrf().disable()
		.authorizeHttpRequests(auth ->
				auth.
				antMatchers("/login", "/", "/buyerSignUp", "/sellerSignUp", "/signin").permitAll()				
				.anyRequest().authenticated()						
				)		
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		
		
		
		httpSecurity.authenticationProvider(this.authenticationProvider());
		
		//JWT STEP :
		httpSecurity.addFilterBefore(this.jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
		DefaultSecurityFilterChain myConfigure = httpSecurity.build();
		return myConfigure;
		
	}


	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsServiceImpl); // Set custom user details service
		authenticationProvider.setPasswordEncoder(this.passwordEncoder()); // Set password encoder
		return authenticationProvider;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Use BCryptPasswordEncoder for password hashing
	}
	

	@Bean
	AuthenticationManager authenticationManager() {
		ProviderManager providerManager = new ProviderManager(Collections.singletonList(authenticationProvider()));
		return providerManager; // Configure and return authentication manager
	}
	
	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//			throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}

}