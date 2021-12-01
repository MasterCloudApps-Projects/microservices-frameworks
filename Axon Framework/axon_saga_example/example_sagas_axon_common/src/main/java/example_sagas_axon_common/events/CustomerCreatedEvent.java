package example_sagas_axon_common.events;

import java.math.BigDecimal;

public class CustomerCreatedEvent {
	
	private String customerId;
	private String name;
	private BigDecimal balance;
	
	public CustomerCreatedEvent(String id, String name, BigDecimal balance) {
		this.customerId = id;
		this.name = name;
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
