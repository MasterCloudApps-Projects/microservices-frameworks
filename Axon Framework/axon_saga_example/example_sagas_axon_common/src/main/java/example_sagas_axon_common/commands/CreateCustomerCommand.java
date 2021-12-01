package example_sagas_axon_common.commands;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateCustomerCommand {

	@TargetAggregateIdentifier
	private String customerId;
	
	private String name;
	
	private BigDecimal balance;
	
	
	public CreateCustomerCommand(String id, String name, BigDecimal balance) {
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


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
