package example_sagas_axon_common.events;

import example_sagas_axon_common.types.OrderStatus;

public class OrderRejectedEvent {

	private String orderId;
	
	private OrderStatus status;
	
	private String rejectedReason;

	public OrderRejectedEvent() {
		super();
	}

	public OrderRejectedEvent(String orderId, OrderStatus status, String rejectedReason) {
		super();
		this.orderId = orderId;
		this.status = status;
		this.rejectedReason = rejectedReason;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public String getRejectedReason() {
		return rejectedReason;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}
	
}
