package com.hashedin.fastkart.form;

import com.hashedin.fastkart.model.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

	private String token;
	private Users user;
	
}
