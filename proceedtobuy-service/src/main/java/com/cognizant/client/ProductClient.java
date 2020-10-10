package com.cognizant.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cognizant.DTO.Product;
//@Headers("Content-Type: application/json")
@FeignClient(name = "productclient",url = "http://localhost:8081/api/",fallback = ProductClientFallback.class)
//@FeignClient(url="http://localhost:8081/api/")
public interface ProductClient{
	@GetMapping("searchProductById/{productId}")
	public  ResponseEntity<Product> searchProductById(@RequestHeader("Authorization") String token,@PathVariable("productId") int productId);

	@GetMapping("test")
	public String test();
}

