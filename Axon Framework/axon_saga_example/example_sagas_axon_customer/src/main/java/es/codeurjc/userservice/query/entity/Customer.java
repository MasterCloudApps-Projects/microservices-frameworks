package es.codeurjc.userservice.query.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	private String customerId;
	
	private String name;
	
	private BigDecimal balance;

	public Customer() {
		super();
	}

	public Customer(String customerId, String name, BigDecimal balance) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.balance = balance;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getBalance() {
		return balance;
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
