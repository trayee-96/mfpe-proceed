package com.cognizant.client;

import org.springframework.http.ResponseEntity;

public class AuthClientFallback implements AuthClient {

	@Override
	public boolean getValidity(String token) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId(String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String test() {
		// TODO Auto-generated method stub
		return null;
	}

}
