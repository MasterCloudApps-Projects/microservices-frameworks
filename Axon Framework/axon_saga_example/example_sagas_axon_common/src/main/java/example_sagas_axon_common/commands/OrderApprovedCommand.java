package example_sagas_axon_common.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import example_sagas_axon_common.types.OrderStatus;



public class OrderApprovedCommand {

	@TargetAggregateIdentifier
	private String orderId;
	
	private OrderStatus status;

	public OrderApprovedCommand() {
		super();
	}

	public OrderApprovedCommand(String orderId, OrderStatus status) {
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
