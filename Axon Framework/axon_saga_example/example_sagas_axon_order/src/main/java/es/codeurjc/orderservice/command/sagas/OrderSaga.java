package es.codeurjc.orderservice.command.sagas;


import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import example_sagas_axon_common.commands.OrderApprovedCommand;
import example_sagas_axon_common.commands.OrderRejectedCommand;
import example_sagas_axon_common.commands.ValidateCustomerPaymentCommand;
import example_sagas_axon_common.types.OrderStatus;
import example_sagas_axon_common.events.InsufficientMoneyEvent;
import example_sagas_axon_common.events.OrderCreatedEvent;
import example_sagas_axon_common.events.ValidatedCustomerPaymentEvent;


@Saga
public class OrderSaga {

	@Autowired
	private transient CommandGateway commandGateway;
    
    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
        String customerId = orderCreatedEvent.getCustomerId();
        //Asociar la saga al siguiente associationProperty
        SagaLifecycle.associateWith("customerId", customerId);
        //Enviar el nuevo commando para accionar
        
        try {
        	commandGateway.sendAndWait(new ValidateCustomerPaymentCommand(customerId, orderCreatedEvent.getPrice(), orderCreatedEvent.getOrderId()));
        	System.out.println("Command: ValidateCustomerPaymentCommand Sended");
          } catch (RuntimeException e) {
            System.out.println("Customer Not Found");
            commandGateway.send(new OrderRejectedCommand(orderCreatedEvent.getOrderId(), OrderStatus.REJECTED, "Customer Not Found"));
            SagaLifecycle.end();
          }
    }
    
    @SagaEventHandler(associationProperty = "customerId", keyName = "customerId")
    public void handle(ValidatedCustomerPaymentEvent validatedCustomerPaymentEvent){
    	System.out.println("Command: ValidatedCustomerPaymentEvent Recivied");
        commandGateway.send(new OrderApprovedCommand(validatedCustomerPaymentEvent.getOrderId(), OrderStatus.APPROVED));
        SagaLifecycle.end();
        System.out.println("Command: ValidateCustomerPaymentCommand Ended");
    }
    
    @SagaEventHandler(associationProperty = "customerId", keyName = "customerId")
    public void handle(InsufficientMoneyEvent insufficientMoneyEvent){
    	System.out.println("Command: insufficientMoneyEvent Recivied");
    	commandGateway.send(new OrderRejectedCommand(insufficientMoneyEvent.getOrderId(), OrderStatus.REJECTED, "Insufficient Money"));
        SagaLifecycle.end();
        System.out.println("Command: ValidateCustomerPaymentCommand Ended");
    }
}
