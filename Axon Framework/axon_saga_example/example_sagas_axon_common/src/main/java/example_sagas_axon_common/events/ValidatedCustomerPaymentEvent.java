package example_sagas_axon_common.events;

import java.math.BigDecimal;

public class ValidatedCustomerPaymentEvent {

	private String orderId;
	
	private BigDecimal price;
	
	private String customerId;

	public ValidatedCustomerPaymentEvent() {
		super();
	}

	public ValidatedCustomerPaymentEvent(String orderId, BigDecimal price, String customerId) {
		super();
		this.orderId = orderId;
		this.price = price;
		this.customerId = customerId;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	public String getCustomerId() {
		return customerId;
	}



	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
