## Eventuate Tram
Es compatible y configurable con Java Spring Boot, Micronaut y Quarkus<br>
Este **framework** funciona mediante el envío de mensajes asíncronos entre los distintos participantes de la saga<br>
Esta formado por cuatro servicios: Apache Zookeeper, Apache Kafka, una Base de datos MySQL y un componente CDC. Todos estos se despliegan con un docker-compose de forma distribuida.

Para implementar una Saga con **Eventuate**, lo primero que debemos hacer es crear una clase que implemente la clase **SimpleSaga** y dentro de la misma definir un orquestador con **SagaDefinition**

**SimpleSaga**:
```
public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {
```

**SagaDefinition**: Builder de sagas u **Orquestador**
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

Este viene siendo el Orquestador, que permite definir los pasos y las compensaciones de la saga. Esta se divide por funciones **step()** que indican cada uno de los pasos.

Dentro de estos pasos hay dos tipos de funciones:

**.invokeLocal**: que indica el llamado a una funciona previamente definida que no requiere comuncación con ningún servicio. 

```
.invokeLocal(this::create)
private void create(CreateOrderSagaData data) {
    Order order = orderService.createOrder(data.getOrderDetails());
    data.setOrderId(order.getId());
}
```

**.invokeParticipant**: indica el llamado a una función que requiere la comunicación con algún otro servicio. 

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

Este tipo de función onsta de un builder send para la creación de comandos que necesitan llegar a otros servicios.

Luego el servicio escucha este comando definiendo así un **CommandHadler**, de esta manera: 
```
public CommandHandlers commandHandlerDefinitions() {
    return SagaCommandHandlersBuilder
            .fromChannel("customerService")
            .onMessage(ReserveCreditCommand.class, this::reserveCredit)
            .build();
}
```

Esto indica que función debe de ejecutarse en caso de recibir un mensaje con la clase (comando) que se indique.


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

Seguido de este este, se puede añadir una o varias funciones de compensación. 

**withCompensation**: Para compensaciones de funciones locales, y en caso de error se ejecutará de manera automatica la función que se indique. 

```
.withCompensation(this::reject)
private void reject(CreateOrderSagaData data) {
    orderService.rejectOrder(data.getOrderId(), data.getRejectionReason());
}
```

Luego para compensaciones que requieran la respuesta de un servicio externo se puede utilizar:

**onReply**: Espera por la respuesta, y en caso de algún error especifico se puede lanzar una función de compensación.

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


