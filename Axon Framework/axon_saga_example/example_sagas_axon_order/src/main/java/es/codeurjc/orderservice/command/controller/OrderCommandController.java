package es.codeurjc.orderservice.command.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.orderservice.command.service.OrderCommandService;
import es.codeurjc.orderservice.dto.CreateOrderRequest;

@RestController
public class OrderCommandController {

	@Autowired
	OrderCommandService orderService;

	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public CompletableFuture<String> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
		return orderService.createOrder(createOrderRequest);
	}
}
