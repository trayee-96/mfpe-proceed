package com.cognizant.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


//@Headers("Content-Type: application/json")
@FeignClient(name = "authclient", url = "http://localhost:8084/authapp/", fallback = AuthClientFallback.class)
//@FeignClient(url="http://localhost:8081/api/")
public interface AuthClient {
	@GetMapping("validate")
	public boolean getValidity(@RequestHeader("Authorization") String token);

	@RequestMapping(value = "/getId", method = RequestMethod.GET)
	public int getId(@RequestHeader("Authorization") String token);

	@GetMapping("test")
	public String test();
}
