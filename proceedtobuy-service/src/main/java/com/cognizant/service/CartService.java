package com.cognizant.service;

import java.util.Date;
import java.util.List;

import com.cognizant.DTO.CartRequest;
import com.cognizant.DTO.Product;
import com.cognizant.DTO.Vendor;
import com.cognizant.exception.AccessUnauthorizedException;
import com.cognizant.exception.ProductNotFoundException;

public interface CartService {
	void addProductToCart(String token, int customerId, int productId, int zipCode, Date expectedDeliveryDate,int quantity) throws ProductNotFoundException;

	public void addProductToWishlist(int customerId, int productId, int quantity);

	public void deleteFromCart(int customerId, int productId) throws ProductNotFoundException;

	public void deleteFromWishlist(int customerId, int productId) throws ProductNotFoundException;

	List<Product> getWishlist(String token, int customerId);

	
	List<CartRequest> getCart(String token, int customerId);

	public void updateCart(int customerId, int productId, int qty);
	public List<Vendor> getVendors(int productId, int qty);

	public void updateVendor(int customerId, int productId, int qty, int vendorId);
	public long getDeliveryCharge(int vendorId,int productId,int qty);
	public float calculateTotalForCart(String token, int customerId);
}
