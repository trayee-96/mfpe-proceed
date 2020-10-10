package com.cognizant.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.DTO.CartRequest;
import com.cognizant.DTO.Product;
import com.cognizant.DTO.Vendor;
import com.cognizant.client.AuthClient;
import com.cognizant.client.ProductClient;
import com.cognizant.client.VendorClient;
import com.cognizant.exception.*;
import com.cognizant.model.Cart;
import com.cognizant.model.VendorWishlist;
import com.cognizant.repository.CartRepository;
import com.cognizant.repository.WishListRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository repository;
	@Autowired
	ProductClient client;
	@Autowired
	WishListRepository wlrepo;
	@Autowired
	AuthClient auth;
	@Autowired
	VendorClient vclient;

	// @Transactional
	@Override
	public void addProductToCart(String token, int customerId, int productId, int zipCode, Date expectedDeliveryDate,int quantity) throws ProductNotFoundException {
		// String test = client.test();
		// log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + test);
		List<Vendor> vendorsAvail = vclient.getVendors(productId, quantity);
		if (vendorsAvail.isEmpty()) {
			addProductToWishlist(customerId, productId, quantity);
		} else {
			log.info("<--------Inside service of Adding product to cart for customer---------->");
			ResponseEntity<Product> p = client.searchProductById(token, productId);
			if (p == null) {
				throw new ProductNotFoundException("Product with id=:" + productId + " was not found.");
			}
			Cart c = repository.findByUserIdAndProductId(customerId, p.getBody().getId());
			if (c != null) {
				c.setCount(c.getCount() + quantity);
				c.setVendorId(vendorsAvail.get(0).getVendorId());
				repository.save(c);
				// return ResponseEntity.status(HttpStatus.OK).body();
			} else {
				Cart cr = new Cart();
				cr.setUserId(customerId);
				cr.setProductId(productId);
				cr.setZipcode(zipCode);
				cr.setVendorId(vendorsAvail.get(0).getVendorId());
				cr.setCount(quantity);
				cr.setDelivery_date(expectedDeliveryDate);
				// 6, customerId, p.getId(), zipCode, expectedDeliveryDate, 15, 1);
				repository.save(cr);
				// return ResponseEntity.status(HttpStatus.OK).body(repository.save(cr));

			}
			log.info("<-------Product Added to cart---------(service method ends)");
		}
	}

	// @Transactional
	@Override
	public void addProductToWishlist(int customerId, int productId, int quantity) {
		log.info("<--------Inside service of Adding product to wishlist for customer---------->");
		VendorWishlist wl = new VendorWishlist();
		wl.setCustomerId(customerId);
		wl.setProductId(productId);
		wl.setQuantity(quantity);
		wl.setAddingDateToWishlist(LocalDate.now());
		wlrepo.save(wl);
		// return ResponseEntity.status(HttpStatus.OK).body(wlrepo.save(new
		// VendorWishlist(v.getVendorId(),productId,quantity,new Date())));
		log.info("<-------Product Added to WishList---------(service method ends)");
	}

	// @Transactional
	@Override
	public void deleteFromCart(int customerId, int productId) throws ProductNotFoundException {
		log.info("<--------Inside service of deleting product from cart---------->");
		Cart c = repository.findByUserIdAndProductId(customerId, productId);
		if (c != null) {
			if (c.getCount() > 1) {
				c.setCount(c.getCount() - 1);
				repository.save(c);

			} else
				repository.delete(c);
			// return ResponseEntity.status(HttpStatus.OK).body("Product with id " +
			// productId + "removed successfully");
		} else
			throw new ProductNotFoundException("Product with id" + productId + "is not in the cart.");
		log.info("<-------Product deleted from cart---------(service method ends)");

	}

	// @Transactional
	@Override
	public void deleteFromWishlist(int customerId, int productId) throws ProductNotFoundException {
		log.info("<--------Inside service of deleting product from wishlist---------->");
		VendorWishlist vwl = wlrepo.findByCustomerIdAndProductId(customerId, productId);
		if (vwl != null) {
			wlrepo.delete(vwl);
			// return ResponseEntity.status(HttpStatus.OK).body("Product with id " +
			// productId + "removed successfully");
		} else
			throw new ProductNotFoundException("Product with id" + productId + "is not in the cart.");
		log.info("<-------Product deleted from wishlist---------(service method ends)");
	}

	@Override
	public List<CartRequest> getCart(String token, int customerId) {
		log.info("<--------Inside service of getting Cart---------->");
		List<CartRequest> cartRequest = new ArrayList<CartRequest>();
		List<Cart> cartList = repository.findByUserId(customerId);
		for (Cart cart : cartList) {
			cartRequest.add(new CartRequest(customerId, client.searchProductById(token, cart.getProductId()).getBody(),
					cart.getZipcode(), cart.getCount(), cart.getDelivery_date()));
		}
		log.info("<-------getCart---------(service method ends)");
		return cartRequest;
	}

	@Override
	public List<Product> getWishlist(String token, int customerId) {
		log.info("<--------Inside service of getting wishlist---------->");
		List<VendorWishlist> list = wlrepo.findByCustomerId(customerId);
		List<Product> products = new ArrayList<Product>();
		for (VendorWishlist wish : list) {
			products.add(client.searchProductById(token, wish.getProductId()).getBody());
		}
		log.info("<-------get wishlist---------(service method ends)");
		return products;
	}

	@Override
	public void updateCart(int customerId, int productId, int qty) {
		log.info("inside update cart/" + customerId + "/" + productId + "/" + qty);
		// ResponseEntity<Product> p = client.searchProductById(token,productId);
		Cart c = repository.findByUserIdAndProductId(customerId, productId);
		if (qty >= 1) {
			if (c != null) {
				c.setCount(qty);
				repository.save(c);
				// return ResponseEntity.status(HttpStatus.OK).body();
			}
		}
		log.info("<-------update cart---------(service method ends)");
	}

	@Override
	public void updateVendor(int customerId, int productId, int qty, int vendorId) {
		log.info("inside update vendor/" + customerId + "/" + productId + "/" + qty + "/" + vendorId);
		List<Vendor> vendors = vclient.getVendors(productId, qty);
		Cart c = repository.findByUserIdAndProductId(customerId, productId);
		for (Vendor vendor : vendors) {
			if (vendor.getVendorId() == vendorId) {
				c.setVendorId(vendorId);
				repository.save(c);
				break;
			}
		}
		log.info("<-------update vendor---------(service method ends)");
	}

	public List<Vendor> getVendors(int productId, int qty) {
		log.info("Getting vendors SERVICE STARTS");
		log.info("Getting vendors SERVICE ENDS");
		return vclient.getVendors(productId, qty);
	}
	public long getDeliveryCharge(int vendorId,int productId,int qty) {
		log.info("START");
		List<Vendor> vendors=getVendors(productId,qty);
		long dcharge=0;
		for (Vendor vendor : vendors) {
			if(vendor.getVendorId()==vendorId) {
				dcharge=vendor.getDeliveryCharge();
				break;
			}
		}
		log.info("END");
		return dcharge;
	}
	public float calculateTotalForCart(String token, int customerId) {
		log.info("START");
		float total=0.0f;
		List<CartRequest> cr= getCart(token,customerId);
		for (CartRequest cartRequest : cr) {
			total+=cartRequest.getProduct().getPrice()*cartRequest.getQty();
		}
		log.info("END");
		return total;
	}
}
