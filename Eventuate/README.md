## Eventuate Tram
This framework supports Java Spring Boot, Micronaut y Quarkus<br>
It works while sending asynchronous message between differents microservices.
It's made with four other technologies: Apache Zookeeper, Apache Kafka, a BBDD MySQL and a CDC component. This is all run with a docker-compose file in a distributed way.

Our example is formed by two microservices: `OrderService` that creates orders and `CustomerService` that manage customers.

In order to implement the Saga Pattern with **Eventuate**, we need to first create a class that implements the **SimpleSaga** interface and within it define an orquestrator with the **SagaDefinition** annotation. We'll do all this on the example inside of the `OrderService`.

**SimpleSaga**:
```
public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {
```

**SagaDefinition**: Builder of Sagas and **Orquestator**
```
 private SagaDefinition<CreateOrderSagaData> sagaDefinition =
          step()
            .invokeLocal(this::create)
            .withCompensation(this::reject)
          .step()
            .invokeParticipant(this::reserveCredit)
            .onReply(CustomerNotFound.class, this::handleCustomerNotFound)
            .onReply(CustomerCreditLimitExceeded.class, this::handleCustomerCreditLimitExceeded)
          .step()
            .invokeLocal(this::approve)
          .build();
```

This would be our Orquestator, that will allow us to define the steps and the compensations of the Saga. This is divided by **step()** functions and indicates each one of the steps.

Insdie of these steps, there's two types of functions:

**.invokeLocal**: which indicates calling of a function previously defined and it won't require any communication between services.

```
.invokeLocal(this::create)
private void create(CreateOrderSagaData data) {
    Order order = orderService.createOrder(data.getOrderDetails());
    data.setOrderId(order.getId());
}
```

**.invokeParticipant**: indicates the call of a function that do requires the communication between another service. In our example, is an action that sends a command to the `CustomerService`. 

```
.invokeParticipant(this::reserveCredit)
private CommandWithDestination reserveCredit(CreateOrderSagaData data) {
    long orderId = data.getOrderId();
    Long customerId = data.getOrderDetails().getCustomerId();
    Double orderTotal = data.getOrderDetails().getOrderTotal();
    return send(new ReserveCreditCommand(customerId, orderId, orderTotal))
            .to("customerService")
            .build();
}
```

This type of function need a builder to create the commands needed to reach out to another service.

Then the receptor listens to this command defined by **CommandHadler**, this way:
```
public CommandHandlers commandHandlerDefinitions() {
    return SagaCommandHandlersBuilder
            .fromChannel("customerService")
            .onMessage(ReserveCreditCommand.class, this::reserveCredit)
            .build();
}
```

This indicates that the function must be executed only when it recieves a message with that command class. The `reserveCredit` method is defined in `CustomerService` and it will be executed after recieving a `ReserveCreditCommand` from the `OrderService`.

```
  public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {
    ReserveCreditCommand cmd = cm.getCommand();
    try {
      customerService.reserveCredit(cmd.getCustomerId(), cmd.getOrderId(), cmd.getOrderTotal());
      return withSuccess(new CustomerCreditReserved());
    } catch (CustomerNotFoundException e) {
      return withFailure(new CustomerNotFound());
    } catch (CustomerCreditLimitExceededException e) {
      return withFailure(new CustomerCreditLimitExceeded());
    }
  }
```
Followed by this, you can add one or many compensation functions.

**withCompensation**: in case of an error the function defined with this will be executed automatically . For example, if Reserve Credit fails, the order will be rejected.

```
.withCompensation(this::reject)
private void reject(CreateOrderSagaData data) {
    orderService.rejectOrder(data.getOrderId(), data.getRejectionReason());
}
```
To manage the responde that could be recieved on each step we can use:

**onReply**: It waits for an specific answer and if its recieved it launches a specific method. In our example, the orquestator waits to the possible errors that the `CustomerService` could send when executing the `reserveCredit` and, if its recieved it fires a function to update the rejectedReason of the order.

```
.invokeParticipant(this::reserveCredit)
            .onReply(CustomerNotFound.class, this::handleCustomerNotFound)
            .onReply(CustomerCreditLimitExceeded.class, this::handleCustomerCreditLimitExceeded)

private void handleCustomerNotFound(CreateOrderSagaData data, CustomerNotFound reply) {
    data.setRejectionReason(RejectionReason.UNKNOWN_CUSTOMER);
}

private void handleCustomerCreditLimitExceeded(CreateOrderSagaData data, CustomerCreditLimitExceeded reply) {
    data.setRejectionReason(RejectionReason.INSUFFICIENT_CREDIT);
}
```

## Compile and launch our application

First we need to compile our application using:

```
./gradlew assemble
```

Then, we launch the different services using [Docker Compose](https://docs.docker.com/compose/), like this:

```
./gradlew mysqlComposeBuild
./gradlew mysqlComposeUp
```

Once it's initialized, the urls will be:

    - order-service:  http://localhost:8081
    - customer-service:  http://localhost:8082
    - api-gateway:  http://localhost:8083


When we finish using it we can delete and stop all the container doing this:

```
    $ ./gradlew mysqlComposeDown
```

## Use cases

We're gonna use curl to the how the application works

Create client
```bash
$ curl -X POST --header "Content-Type: application/json" -d '{
  "creditLimit": 5,
  "name": "Name Lastname"
}' http://localhost:8082/customers

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8

{
  "customerId": 1
}
```

Create an order
```bash
$ curl -X POST --header "Content-Type: application/json" -d '{
  "customerId": 1,
  "orderTotal": 4
}' http://localhost:8081/orders

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8

{
  "orderId": 1
}
```

Check the state of the order
```bash
$ curl -X GET http://localhost:8081/orders/1

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8

{
  "orderId": 1,
  "orderState": "APPROVED"
}