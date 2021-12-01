package es.codeurjc.orderservice.command.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import example_sagas_axon_common.commands.CreateOrderCommand;
import example_sagas_axon_common.types.OrderStatus;
import es.codeurjc.orderservice.dto.CreateOrderRequest;

@Service
public class OrderCommandService {

	private final CommandGateway commandGateway;
	
	public OrderCommandService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
	
	public CompletableFuture<String> createOrder(CreateOrderRequest createOrderRequest) {
		return this.commandGateway.send(
					new CreateOrderCommand(
							UUID.randomUUID().toString(),
							OrderStatus.PENDING, 
							createOrderRequest.getPrice(), 
							createOrderRequest.getCustomerId()
							)
				);
	}
}
