package com.cognizant.client;

import org.springframework.http.ResponseEntity;

import com.cognizant.DTO.Product;

public class ProductClientFallback implements ProductClient{

	@Override
	public String test() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Product> searchProductById(String token, int productId) {
		// TODO Auto-generated method stub
		return null;
	}

}
