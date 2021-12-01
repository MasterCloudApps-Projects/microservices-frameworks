package es.codeurjc.orderservice.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import example_sagas_axon_common.commands.CreateOrderCommand;
import example_sagas_axon_common.commands.OrderApprovedCommand;
import example_sagas_axon_common.commands.OrderRejectedCommand;
import example_sagas_axon_common.types.OrderStatus;
import example_sagas_axon_common.events.OrderApprovedEvent;
import example_sagas_axon_common.events.OrderCreatedEvent;
import example_sagas_axon_common.events.OrderRejectedEvent;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
    private String orderId;
	
	private OrderStatus state;
	
	private BigDecimal price;
	
	private String customerId;
	
	private String rejectedReason;
	
	public OrderAggregate() {
	}

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand){
        AggregateLifecycle.apply(
        		new OrderCreatedEvent(
					createOrderCommand.getOrderId(),
	                createOrderCommand.getPrice(),
	                createOrderCommand.getCustomer(),
	                createOrderCommand.getState()
                )
        );
    }
    
    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
    	this.orderId = orderCreatedEvent.getOrderId();
    	this.state = orderCreatedEvent.getStatus();
    	this.price = orderCreatedEvent.getPrice();
    	this.customerId = orderCreatedEvent.getCustomerId();
    }
    
    @CommandHandler
    public void handle(OrderRejectedCommand orderRejectedCommand){
        AggregateLifecycle.apply(
        		new OrderRejectedEvent(
        				orderRejectedCommand.getOrderId(),
        				orderRejectedCommand.getStatus(),
        				orderRejectedCommand.getRejectedReason()
                )
        );
    }
    
    
    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
    	this.state = orderRejectedEvent.getStatus();
    	this.rejectedReason = orderRejectedEvent.getRejectedReason();
    }
    
    @CommandHandler
    public void handle(OrderApprovedCommand orderApprovedCommand){
        AggregateLifecycle.apply(
        		new OrderApprovedEvent(
        				orderApprovedCommand.getOrderId(),
        				orderApprovedCommand.getStatus()
                )
        );
    }
    
    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
    	this.state = orderApprovedEvent.getStatus();
    }
}
