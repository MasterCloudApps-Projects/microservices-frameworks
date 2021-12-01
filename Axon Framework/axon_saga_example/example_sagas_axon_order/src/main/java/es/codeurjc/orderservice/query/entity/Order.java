package es.codeurjc.orderservice.query.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import example_sagas_axon_common.types.OrderStatus;

@Entity
@Table(name="orders") 
public class Order {

	@Id
	private String orderId;
	
	private OrderStatus state;
	
	private BigDecimal price;
	
	private String customerId;
	
	private String rejectedReason;
	
	public Order() {
		super();
	}
	
	public Order(String orderId, OrderStatus state, BigDecimal price, String customerId, String rejectedReason) {
		super();
		this.orderId = orderId;
		this.state = state;
		this.price = price;
		this.customerId = customerId;
		this.rejectedReason = rejectedReason;
	}

	public String getOrderId() {
		return orderId;
	}

	public OrderStatus getState() {
		return state;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getRejectedReason() {
		return rejectedReason;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setState(OrderStatus state) {
		this.state = state;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}
}
