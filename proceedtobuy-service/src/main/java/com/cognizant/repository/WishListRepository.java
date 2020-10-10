package com.cognizant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.model.VendorWishlist;
@Repository
public interface WishListRepository extends JpaRepository<VendorWishlist, Integer>{

	List<VendorWishlist> findByCustomerId(int customerId);

	VendorWishlist findByCustomerIdAndProductId(int customerId, int productId);

}
