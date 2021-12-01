package example_sagas_axon_common.events;

import java.math.BigDecimal;

import example_sagas_axon_common.types.OrderStatus;



public class OrderCreatedEvent {

	private String orderId;
	
    private BigDecimal price;
	
	private String customerId;

    public final OrderStatus status;

    public OrderCreatedEvent(String orderId, BigDecimal price, String customerId, OrderStatus status) {
    	this.orderId = orderId;
        this.price = price;
        this.customerId = customerId;
        this.status = status;
    }

	public BigDecimal getPrice() {
		return price;
	}

	public String getCustomerId() {
		return customerId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
