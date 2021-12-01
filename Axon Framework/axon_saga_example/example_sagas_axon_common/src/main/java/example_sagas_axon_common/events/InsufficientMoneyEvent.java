package example_sagas_axon_common.events;

public class InsufficientMoneyEvent {

	private String orderId;
	
	private String customerId;

	public InsufficientMoneyEvent() {
		super();
	}

	public InsufficientMoneyEvent(String orderId, String customerId) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}

