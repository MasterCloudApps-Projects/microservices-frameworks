package example_sagas_axon_common.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ValidateCustomerPaymentCommand {

	@TargetAggregateIdentifier
	private String customerId;
	
	private BigDecimal price;
	
	private String orderId;
	
	public ValidateCustomerPaymentCommand() {
		super();
	}
	
	public ValidateCustomerPaymentCommand(String customerId, BigDecimal price, String orderId) {
		super();
		this.customerId = customerId;
		this.price = price;
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
