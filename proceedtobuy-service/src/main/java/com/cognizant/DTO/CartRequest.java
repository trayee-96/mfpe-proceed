package com.cognizant.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
	private int customerId;
	private Product product;
	private int qty;
	private int zipCode;
	private Date expectedDeliveryDate;
//	public CartRequest(int customerId, int productId, int zipCode, String expectedDeliveryDate) throws ParseException {
//		super();
//		this.customerId = customerId;
//		this.productId = productId;
//		this.zipCode = zipCode;
//		this.expectedDeliveryDate = new SimpleDateFormat("dd/MM/yyyy").parse(expectedDeliveryDate);
//	}
	
}
