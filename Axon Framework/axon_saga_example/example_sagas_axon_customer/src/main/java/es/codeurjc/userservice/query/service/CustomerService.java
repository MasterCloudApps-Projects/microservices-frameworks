package es.codeurjc.userservice.query.service;

import java.math.BigDecimal;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example_sagas_axon_common.events.CustomerCreatedEvent;
import example_sagas_axon_common.events.ValidatedCustomerPaymentEvent;
import es.codeurjc.userservice.query.entity.Customer;
import es.codeurjc.userservice.query.queries.FindCustomerByIdQuery;
import es.codeurjc.userservice.query.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@EventHandler
	public void on(CustomerCreatedEvent customerCreatedEvent) {
		Customer customer = new Customer(customerCreatedEvent.getCustomerId(), customerCreatedEvent.getName(), customerCreatedEvent.getBalance());
		customerRepository.save(customer);
	}
	
	@EventHandler
	public void on(ValidatedCustomerPaymentEvent validatedCustomerPaymentEvent) {
		Customer customer = customerRepository.getById(validatedCustomerPaymentEvent.getCustomerId());
		BigDecimal newBalance = customer.getBalance().subtract(validatedCustomerPaymentEvent.getPrice());
		customer.setBalance(newBalance);
		customerRepository.save(customer);
	}
	
	@QueryHandler
	public Customer handle(FindCustomerByIdQuery findCustomerByIdQuery) {
		return customerRepository.findById(findCustomerByIdQuery.getId()).orElse(null);
	}

}
