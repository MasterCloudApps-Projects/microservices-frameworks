# Axon Framework
It supports Java with Spring Boot<br>
This **framework** has a package that helps us define and create CQRS arquitectures easily.

Here we're gonna explain the basics of the Axon documentation and how to implement it.

## Documentation
The main functions and command are:
**@Aggregate**: This is an object that has a state and methods to change it's own state. By default, Axon configures the aggregators with Event Sourcing.

```
@Aggregate
public class CustomerAggregate () {
```

**@AggregateIdentifier**: declares the id of an aggregator.

```
@AggregateIdentifier
private String customerId;
```

**@CommandHandler**: identifies a function as command hanlder. This allow us to manage certain commands to send events.
```
@CommandHandler
public void handle(ValidateCustomerPaymentCommand validateCustomerPaymentCommand) {
```

**Note**: the first command defined inside of an Aggregator, that it should be the one needed to create itself, it should be written this way:

```@CommandHandler
public CustomerAggregate(CreateCustomerCommand createCustommerCommand) {
```

**@EventSourcingHandler**: This annotation it's used to manage events. On the aggregators its used to update it's own state. And it can also be used on services and Sagas to exectue an action or command.

**Aggregator**

```
@EventHandler
public void on(CustomerCreatedEvent customerCreatedEvent) { 
    this.customerId = customerCreatedEvent.getCustomerId();
    this.name = customerCreatedEvent.getName();
```

**Service**

```
@EventHandler
public void on(OrderCreatedEvent orderCreatedEvent) {
    Order order = new Order(nueva orden); 
    orderRepository.save(order);
}
```

**@QueryHandler**: this annotation allows us to define a function to handle queries.

```
@QueryHandler<br>
public Order handle(FindOrderByIdQuery findOrderByIdQuery) {
```

Axon aslo gives us gateways that we can use to emit events, queries and commands. 

**CommandGateway**: it's used to send commands. We have .send (async) and .sendAndWait (sync)

```
commandGateway.send(COMMAND);
```

**AggregateLifecycle**: this allow us change the state of the aggregate and the emit an event.

```
AggregateLifecycle.apply(new OrderCreatedEvent())
```

### Sagas
The sagas in Axon are implemented this way:

**@Saga**: Annotation to define a Saga.

```
@Saga
public class OrderSaga {
```

**@StartSaga**: Annotation that defined the function that is gonna fire the Saga.

```
@StartSaga
public void handle(OrderCreatedEvent orderCreatedEvent){
```

**@SagaEventHandler**: It's used to define functions of a saga, they're defined by functions that recieved a specific Event class.

```
@SagaEventHandler(associationProperty = "orderId")
public void handle(OrderCreatedEvent orderCreatedEvent){
```

Then, inside of a saga we can change the state of it or ended it this way:

```
SagaLifecycle.associateWith('nuevo id');
SagaLifecycle.end();
```
In case of using **associateWith**, it should pass the id of the aggreator that is going to refer to on the next step of the saga.

```
@SagaEventHandler(associationProperty = "orderId")
```

### Axon Server

In order to use Sagas on this framework we need to have an active Axon Server.

This give us a BBDD where it automatically save all state history of the aggregators, and stablished a communication channel between the different services.

In order to start an Axon Server locally, we need to run this:

> docker run -t --name my-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

And on the client we need to this dependency to the .pom:

```
<dependency>
    <groupId>org.axonframework</groupId> 
    <artifactId>axon-spring-boot-starter</artifactId> 
    <version>4.0.3</version>
</dependency>
``` 

[Axon Server](https://docs.axoniq.io/reference-guide/axon-server/introduction) it can be configured and it can be implemented in Kubernetes too for more configuration and network policies.

## Implementation

First of all, we create the aggregators Customer and Order.

```
@Aggregate
public class CustomerAggregate {

	@AggregateIdentifier
    private String customerId;
```

```
@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
    private String orderId;
```

Then, we create a Saga called **OrderSaga**.

With this, we defined our application flow:

We need to define the start of a Saga, and will be when we create an order:
```
@StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
```

Create the structure of a saga defining the event that we're gonna recieve and the command that we're gonna send to communicate the Order Service and the Customer Service. Among this we have: OrderCreatedEvent, ValidatedCustomerPaymentEvent,InsufficientMoneyEvent, OrderRejectedCommand, OrderApprovedCommand.

## Compile and launch the application

### Start the Axon Server
This is need to perist the state of the aggregators and for the communication between services using event sourcing.

To run our server:
> docker run -t --name my-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

### Launch Customer and Order service
After running the Axon Server, it's time to run each services that will connect to Axon Server:
> /order mvn spring-boot:run
> /customer mvn spring-boot:run

## Use cases
To test the app we can use POSTMAN with this URLs:

**Create Customer POST (http://localhost:8081/create)**:

body:
```
{
    "name": "Stefano Lagattolla",
    "balance": 18000
}
```
**Create Order POST (http://localhost:8080/order)**:

body:
```
{
    "price": 1500,
    "customerId": "3ce57fdf-a5d0-468d-8f42-6dea737819e52"
}
```
**Get Order GET (http://localhost:8080/get_order?id=orderid)**

body:
```
{
    "id": orderid,
    "state": "APPROVED"
    "price": 1500,
    "customerId": "3ce57fdf-a5d0-468d-8f42-6dea737819e52",
    "rejectedReason": ""
    
}
```

**Get Customer GET (http://localhost:8081/get_customer?id=customerid)**

body:
```
{
    "id": customerid,
    "name": "Stefano",
    "balance": 1500
}
```


