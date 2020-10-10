package com.cognizant.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.DTO.Vendor;

@FeignClient(name="vendorclient",url="http://localhost:8083/api")
public interface VendorClient {
	@GetMapping("/getVendorDetails/{productId/{quantity}")
	public List<Vendor> getVendors(@PathVariable int productId,@PathVariable int quantity);
}
