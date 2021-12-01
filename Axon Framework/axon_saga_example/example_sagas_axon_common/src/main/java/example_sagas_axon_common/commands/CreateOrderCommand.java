package example_sagas_axon_common.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import example_sagas_axon_common.types.OrderStatus;



public class CreateOrderCommand {

	@TargetAggregateIdentifier
	private String orderId;
	
	private OrderStatus state;
	
	private BigDecimal price;
	
	private String customer;
	
	public CreateOrderCommand(String id, OrderStatus state, BigDecimal price, String customer) {
		this.orderId = id;
		this.state = state;
		this.price = price;
		this.customer = customer;
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

	public String getCustomer() {
		return customer;
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

	public void setCustomer(String customer) {
		this.customer = customer;
	}


}
