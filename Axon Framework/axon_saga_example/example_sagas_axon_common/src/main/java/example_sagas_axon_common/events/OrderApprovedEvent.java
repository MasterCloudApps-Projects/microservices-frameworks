package example_sagas_axon_common.events;

import example_sagas_axon_common.types.OrderStatus;

public class OrderApprovedEvent {
		
	private String orderId;
	
	private OrderStatus status;
	

	public OrderApprovedEvent() {
		super();
	}

	public OrderApprovedEvent(String orderId, OrderStatus status) {
		super();
		this.orderId = orderId;
		this.status = status;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	
}
