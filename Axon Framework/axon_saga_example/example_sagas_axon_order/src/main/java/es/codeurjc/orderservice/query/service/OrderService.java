package es.codeurjc.orderservice.query.service;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example_sagas_axon_common.events.OrderApprovedEvent;
import example_sagas_axon_common.events.OrderCreatedEvent;
import example_sagas_axon_common.events.OrderRejectedEvent;
import es.codeurjc.orderservice.query.entity.Order;
import es.codeurjc.orderservice.query.queries.FindOrderByIdQuery;
import es.codeurjc.orderservice.query.repository.OrderRepository;


@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@EventHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		Order order = new Order(orderCreatedEvent.getOrderId(), orderCreatedEvent.getStatus(), orderCreatedEvent.getPrice(), orderCreatedEvent.getCustomerId(), "");
		orderRepository.save(order);
	}
	
	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		Order order = orderRepository.getById(orderRejectedEvent.getOrderId());
		order.setState(orderRejectedEvent.getStatus());
		order.setRejectedReason(orderRejectedEvent.getRejectedReason());
		orderRepository.save(order);
	}
	
	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		Order order = orderRepository.getById(orderApprovedEvent.getOrderId());
		order.setState(orderApprovedEvent.getStatus());
		orderRepository.save(order);
	}
	
	@QueryHandler
	public Order handle(FindOrderByIdQuery findOrderByIdQuery) {
		return orderRepository.findById(findOrderByIdQuery.getId()).orElse(null);
	}
}
