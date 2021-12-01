package es.codeurjc.userservice.command.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example_sagas_axon_common.commands.CreateCustomerCommand;
import es.codeurjc.userservice.dto.CreateCustomerRequest;

@Service
public class CustomerCommandService {
	
	@Autowired
	private CommandGateway commandGateway;
	
	public CompletableFuture<String> createCustomer(CreateCustomerRequest createCustomerRequest) {
		return this.commandGateway.send(
					new CreateCustomerCommand(
							UUID.randomUUID().toString(),
							createCustomerRequest.getName(),
							createCustomerRequest.getBalance()
							)
				);
	}

}
