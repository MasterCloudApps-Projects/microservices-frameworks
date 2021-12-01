package es.codeurjc.userservice.query.controller;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.userservice.query.entity.Customer;
import es.codeurjc.userservice.query.queries.FindCustomerByIdQuery;

@RestController
public class CustomerControllerQuery {
	private QueryGateway queryGateway;
	
	public CustomerControllerQuery(QueryGateway queryGateway) {
		super();
		this.queryGateway = queryGateway;
	}
	
	@GetMapping("/get_customer")
    public ResponseEntity<Customer> getAccount(@RequestParam String id) {
		Customer customer = queryGateway.query(
                new FindCustomerByIdQuery(id), Customer.class
        ).join();

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
