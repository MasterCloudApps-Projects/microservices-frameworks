package es.codeurjc.orderservice.dto;

import java.math.BigDecimal;

public class CreateOrderRequest {

	public BigDecimal price;
	
	public String customerId;
	
	public CreateOrderRequest() {

	}

	public CreateOrderRequest(BigDecimal price, String customerId) {
		this.price = price;
		this.customerId = customerId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
