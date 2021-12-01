package es.codeurjc.userservice.dto;

import java.math.BigDecimal;

public class CreateCustomerRequest {

	private String name;
	
	private BigDecimal balance;
	
	public CreateCustomerRequest(String name, BigDecimal balance) {
		super();
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
	
}
