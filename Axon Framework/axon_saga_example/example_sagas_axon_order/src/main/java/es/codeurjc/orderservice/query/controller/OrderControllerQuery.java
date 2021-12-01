package es.codeurjc.orderservice.query.controller;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.orderservice.query.entity.Order;
import es.codeurjc.orderservice.query.queries.FindOrderByIdQuery;

@RestController
public class OrderControllerQuery {
	private QueryGateway queryGateway;
	
	public OrderControllerQuery(QueryGateway queryGateway) {
		super();
		this.queryGateway = queryGateway;
	}
	
	@GetMapping("/get_order")
    public ResponseEntity<Order> getAccount(@RequestParam String id) {
		Order order = queryGateway.query(
                new FindOrderByIdQuery(id), Order.class
        ).join();

        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
