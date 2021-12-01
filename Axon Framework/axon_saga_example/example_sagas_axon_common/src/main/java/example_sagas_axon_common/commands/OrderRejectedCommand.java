package example_sagas_axon_common.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import example_sagas_axon_common.types.OrderStatus;



public class OrderRejectedCommand {
	
	@TargetAggregateIdentifier
	private String orderId;
	
	private OrderStatus status;
	
	private String rejectedReason;

	public OrderRejectedCommand() {
		super();
	}

	public OrderRejectedCommand(String orderId, OrderStatus status, String rejectedReason) {
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
