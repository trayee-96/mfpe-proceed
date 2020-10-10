package com.cognizant.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.DTO.CartRequest;
import com.cognizant.DTO.Product;
import com.cognizant.DTO.VendorStock;
import com.cognizant.client.AuthClient;
import com.cognizant.client.VendorClient;
import com.cognizant.exception.AccessUnauthorizedException;
import com.cognizant.exception.CartNotFoundException;
import com.cognizant.exception.ProductNotFoundException;
import com.cognizant.exception.WishlistNotFoundException;
import com.cognizant.model.Cart;
import com.cognizant.model.VendorWishlist;
import com.cognizant.service.CartService;

@RestController
@RequestMapping("/api")
public class ProceedController {
	@Autowired
	private CartService service;
	@Autowired
	private AuthClient authClient;
	@Autowired
	VendorClient vendorClient;

	@PostMapping("/addProductToCart/{customerId}/{productId}/{zipCode}/{expectedDeliveryDate}")
	public ResponseEntity<?> addProductToCart(@RequestHeader("Authorization") String token,
			@PathVariable int customerId, @PathVariable int productId, @PathVariable int zipCode,
			@PathVariable String expectedDeliveryDate)
			throws ParseException, ProductNotFoundException, AccessUnauthorizedException {

		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		//int vendorId=vendorClient.getVendors(productId, 1).get(0).getVendorId();
		service.addProductToCart(token,customerId, productId, zipCode,
				new SimpleDateFormat("dd-MM-yyyy").parse(expectedDeliveryDate),1);
		return new ResponseEntity<String>("Added to cart.", HttpStatus.CREATED);
	}

	@GetMapping("/getCart/{customerId}")
	public ResponseEntity<?> getCart(@RequestHeader("Authorization") String token, @PathVariable int customerId)
			throws CartNotFoundException, AccessUnauthorizedException {

		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		List<CartRequest> list = service.getCart(token,customerId);
		if (list.isEmpty())
			throw new CartNotFoundException("Cart for user:=" + customerId + " was not found.");
		return new ResponseEntity<List<CartRequest>>(list, HttpStatus.OK);
	}

	@GetMapping("/getWishlist/{customerId}")
	public ResponseEntity<?> getWishlist(@RequestHeader("Authorization") String token, @PathVariable int customerId)
			throws WishlistNotFoundException, AccessUnauthorizedException {

		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		List<Product> list = service.getWishlist(token,customerId);
		if (list.isEmpty())
			throw new WishlistNotFoundException("Wishlist for user:=" + customerId + " was not found.");
		return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
	}

	@DeleteMapping("/deleteProductFromCart/{customerId}/{productId}")
	public ResponseEntity<?> deleteProductFromCart(@RequestHeader("Authorization") String token,
			@PathVariable int customerId, @PathVariable int productId) throws ProductNotFoundException, AccessUnauthorizedException {

		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		service.deleteFromCart(customerId, productId);
		return new ResponseEntity<String>("Product with id " + productId + "removed successfully", HttpStatus.OK);
	}
	@PutMapping("/updateCart/{customerId}/{productId}/{qty}")
	public ResponseEntity<?> updateCart(@RequestHeader("Authorization") String token, @PathVariable int customerId,
			@PathVariable int productId, @PathVariable int qty) throws AccessUnauthorizedException {
		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		service.updateCart(customerId, productId,qty);
		return new ResponseEntity<String>("Cart updated successfully", HttpStatus.OK);
	}

	@DeleteMapping("/deleteProductFromWishlist/{customerId}/{productId}")
	public ResponseEntity<?> deleteProductFromWishlist(@RequestHeader("Authorization") String token,
			@PathVariable int customerId, @PathVariable int productId) throws ProductNotFoundException, AccessUnauthorizedException {

		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		service.deleteFromWishlist(customerId, productId);
		return new ResponseEntity<String>("Product with id " + productId + "removed successfully", HttpStatus.OK);
	}

	@PostMapping("/addProductToWishlist/{customerId}/{productId}/{quantity}")
	public ResponseEntity<?> addProductToWishList(@RequestHeader("Authorization") String token,
			@PathVariable int customerId, @PathVariable int productId, @PathVariable int quantity) throws AccessUnauthorizedException {

		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		service.addProductToWishlist(customerId, productId, quantity);
		return new ResponseEntity<String>("Added to wishlist.", HttpStatus.CREATED);
	}

	@GetMapping("/test}")
	public String test(@PathVariable int customerId, @PathVariable int productId, @PathVariable int quantity) {
		return "OK";
	}
	//update vendor
	@PutMapping("/updateVendor/{customerId}/{productId}/{qty}/{vendorId}")
	public ResponseEntity<?> updateVendor(@RequestHeader("Authorization")String token,@PathVariable int customerId,@PathVariable int productId,@PathVariable int qty,@PathVariable int vendorId) throws AccessUnauthorizedException 
	{
		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		service.updateVendor(customerId, productId,qty,vendorId);
		return new ResponseEntity<String>("Vendor updated successfully", HttpStatus.OK);
	}
	//to get the vendor charge
	@PostMapping("/charge/{vendorId}/{productId}/{qty}")
	public ResponseEntity<?> getDeliveryCharge(@PathVariable int vendorId,@PathVariable int productId,@PathVariable int qty) {
		
		return ResponseEntity.status(HttpStatus.OK).body(service.getDeliveryCharge(vendorId,productId,qty));
	}
	//calculate total cart amount except delivery charge
	@GetMapping("/total/{customerId}")
	public ResponseEntity<?> calculateTotalForCart(@RequestHeader("Authorization")String token, int customerId) throws AccessUnauthorizedException {
		if (authClient.getValidity(token) == false) {
			throw new AccessUnauthorizedException("Invalid token");
		}
		return ResponseEntity.status(HttpStatus.OK).body(service.calculateTotalForCart(token,customerId));
	}
	
}
