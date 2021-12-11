## Eventuate Tram
Es compatible y configurable con Java Spring Boot, Micronaut y Quarkus<br>
Este **framework** funciona mediante el envío de mensajes asíncronos entre los distintos participantes de la saga<br>
Esta formado por cuatro servicios: Apache Zookeeper, Apache Kafka, una Base de datos MySQL y un componente CDC. Todos estos se despliegan con un docker-compose de forma distribuida.

Nuestro ejemplo está formado por dos microservicios: `OrderService` que crea pedidos y `CustomerService` que gestiona a los clientes.

Para implementar una Saga con **Eventuate**, lo primero que debemos hacer es crear una clase que implemente la interfaz **SimpleSaga** y dentro de la misma definir un orquestador con **SagaDefinition**. Todo esto lo haremos en nuestro ejemplo en `OrderService`.

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

**.invokeParticipant**: indica el llamado a una función que requiere la comunicación con algún otro servicio. En nuestro ejemplo, es una acción que enviará un comando al `CustomerService`.

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

Este tipo de función consta de un builder send para la creación de comandos que necesitan llegar a otros servicios.

Luego el servicio receptor escucha este comando definiendo así un **CommandHadler**, de esta manera: 
```
public CommandHandlers commandHandlerDefinitions() {
    return SagaCommandHandlersBuilder
            .fromChannel("customerService")
            .onMessage(ReserveCreditCommand.class, this::reserveCredit)
            .build();
}
```

Esto indica que función debe de ejecutarse en caso de recibir un mensaje con la clase (comando) que se indique. El método `reserveCredit` que vemos a continuación está definido en `CustomerService` y se ejecuta al recibir un `ReserveCreditCommand` del `OrderService`.


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

**withCompensation**: en caso de error se ejecutará de manera automatica la función que se indique. En este caso, si falla la reserva de crédito del `CustomerService`, el pedido se rechaza.

```
.withCompensation(this::reject)
private void reject(CreateOrderSagaData data) {
    orderService.rejectOrder(data.getOrderId(), data.getRejectionReason());
}
```

Para gestionar la respuesta que devuelve alguno de los pasos podemos usar:

**onReply**: Espera por una respuesta específica y si la recibe lanza la ejecución de un método. En nuestro ejemplo, el orquestador espera a las posibles excepciones que puede lanzar el `CustomerService` al ejecutar el `reserveCredit` y, si alguna de las dos excepciones llega, lanza un método para actualizar el motivo del rechazo del pedido.

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

## Compilar y lanzar la aplicación

Primero debemos compilar la aplicación, usamos para ello:

```
./gradlew assemble
```

Después, desplegamos los distintos servicios, usando para ello [Docker Compose](https://docs.docker.com/compose/):

```
./gradlew mysqlComposeBuild
./gradlew mysqlComposeUp
```

Una vez que se ha iniciado nuestra aplicación, podemos las urls de nuestros servicios serán:

    - order-service:  http://localhost:8081
    - customer-service:  http://localhost:8082
    - api-gateway:  http://localhost:8083


Cuando hayamos terminado la ejecución, podemos parar y eliminar los contenedores creados por el docker-compose con:

```
    $ ./gradlew mysqlComposeDown
```

## Ejemplos de uso

Vamos a utilizar curl para ver cómo funciona esta aplicación.

Crear un cliente
```bash
$ curl -X POST --header "Content-Type: application/json" -d '{
  "creditLimit": 5,
  "name": "Nombre Apellido"
}' http://localhost:8082/customers

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8

{
  "customerId": 1
}
```

Crear un pedido
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

Comprobar el estado del pedido que hemos creado
```bash
$ curl -X GET http://localhost:8081/orders/1

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8

{
  "orderId": 1,
  "orderState": "APPROVED"
}