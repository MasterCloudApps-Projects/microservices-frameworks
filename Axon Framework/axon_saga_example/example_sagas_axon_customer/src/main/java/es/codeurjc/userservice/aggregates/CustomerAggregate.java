package es.codeurjc.userservice.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import example_sagas_axon_common.commands.CreateCustomerCommand;
import example_sagas_axon_common.commands.ValidateCustomerPaymentCommand;
import example_sagas_axon_common.events.CustomerCreatedEvent;
import example_sagas_axon_common.events.InsufficientMoneyEvent;
import example_sagas_axon_common.events.ValidatedCustomerPaymentEvent;


@Aggregate
public class CustomerAggregate {

	@AggregateIdentifier
    private String customerId;
	
	private String name;
	
	private BigDecimal balance;
	
	public CustomerAggregate() {
		
	}
	
	@CommandHandler
	public CustomerAggregate(CreateCustomerCommand createCustommerCommand) {
		AggregateLifecycle.apply(new CustomerCreatedEvent(
				createCustommerCommand.getCustomerId(),
				createCustommerCommand.getName(),
				createCustommerCommand.getBalance()));
	}
	
	@EventSourcingHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        customerId = customerCreatedEvent.getCustomerId();
        name = customerCreatedEvent.getName();
        balance = customerCreatedEvent.getBalance();
    }
	
	@CommandHandler
	public void handle(ValidateCustomerPaymentCommand validateCustomerPaymentCommand) {
		if(validateCustomerPaymentCommand.getPrice().compareTo(balance) == -1 ) {
			AggregateLifecycle.apply(new ValidatedCustomerPaymentEvent(
					validateCustomerPaymentCommand.getOrderId(),validateCustomerPaymentCommand.getPrice(), validateCustomerPaymentCommand.getCustomerId()
					)
			);
		}else {
			AggregateLifecycle.apply(new InsufficientMoneyEvent(validateCustomerPaymentCommand.getOrderId(), validateCustomerPaymentCommand.getCustomerId()));
		}
	}
	
	@EventSourcingHandler
	public void on(ValidatedCustomerPaymentEvent validatedCustomerPaymentEvent) {
        balance = balance.subtract(validatedCustomerPaymentEvent.getPrice());
    }
	
}
