## Axon Framework
Es compatible y configurable con Java y Spring Boot<br>
Este **framework** tiene un paquete que te permite definir y crear arquitectura CQRS de manera rápida y sencilla.<br>
Las principales funciones y comandos de integración e implementación de este framework para **Spring Boot** son:<br>
**@Aggregate**: es un objeto que contiene un estado y metodos para alterar este estado. Por defecto, Axon configura los agregadores como 'Event Sourced'.

```
@Aggregate
public class CustomerAggregate () {
```

**@AggregateIdentifier**: te permite definir el identificador de un agregador.

```
@AggregateIdentifier
private String customerId;
```

**@CommandHandler**: Anotación para identificar una función como un manejador de un comando específico. Esto permite manejar los comandos para luego emitir eventos.
```
@CommandHandler
public void handle(ValidateCustomerPaymentCommand validateCustomerPaymentCommand) {
```

**Nota**: El primer comando, que sería el primer comando para crear la primera instancia del agregador, debe de estar anotado de esta manera:

```@CommandHandler
public CustomerAggregate(CreateCustomerCommand createCustommerCommand) {
```

**@EventSourcingHandler**: Esta anotación se utiliza para manejar un evento. Estas funciones se utlizan en los agregadores para auto-actualizar su propio estado. También se pueden usar en servicios o sagas para ejecutar una axión u comando luego de recibir un evento específico.

**agregador**

```
@EventHandler
public void on(CustomerCreatedEvent customerCreatedEvent) { 
    this.customerId = customerCreatedEvent.getCustomerId();
    this.name = customerCreatedEvent.getName();
```

**Servicio**

```
@EventHandler
public void on(OrderCreatedEvent orderCreatedEvent) {
    Order order = new Order(nueva orden); 
    orderRepository.save(order);
}
```

**@QueryHandler**: esto es una anotación que nos permite definir una función para manejar los queries.

```
@QueryHandler<br>
public Order handle(FindOrderByIdQuery findOrderByIdQuery) {
```

Axon también nos promorciona gateways que podemos utilizar para emitir queries, comandos y eventos.

**CommandGateway**: Sirve para enviar comandos. Tenemos .send (async) y sendAndWait (sync).

```
commandGateway.send(COMANDO);
```

**AggregateLifecycle**: Este nos permite cambiar el ciclo de vida de un agregador y emitir un evento.

```
AggregateLifecycle.apply(new OrderCreatedEvent())
```

**QueryGateway**: Permite emitir una query para ser escuchada por el @QueryHandler

```
queryGateway.query(new FindOrderByIdQuery())
```

### Sagas
Las sagas en axon se implementan de esta manera y con estas anotaciones:

**@Saga**: nos sirve para distinguir que una clase o servicio es de tipo Saga.

```
@Saga
public class OrderSaga {
```

**@StartSaga**: esto es una anotación que sirve para definir una función específica como la que va a iniciar la Saga.

```
@StartSaga
public void handle(OrderCreatedEvent orderCreatedEvent){
```

**@SagaEventHandler**: se utiliza para definir, dentro de una saga, una función que reciba o se ejecute al recibir un tipo de evento específico.

```
@SagaEventHandler(associationProperty = "orderId")
public void handle(OrderCreatedEvent orderCreatedEvent){
```

Luego tenemos dentro de las sagas un serivicio que nos proporciona Axon, que te permite cambiar el estado de la saga y seguir su camino respectivo (ya sea el siguiente paso o terminarla).

```
SagaLifecycle.associateWith('nuevo id');
SagaLifecycle.end();
```

En caso de usar el **associateWith**, debe de pasarse el id del agregador al que va a ser referencia el proximo paso y evento a lanzarse. Luego dentro del eventHandler se define que propiedad de asociación se utilizará.

```
@SagaEventHandler(associationProperty = "orderId")
```

## Axon Server

Para el uso de Sagas y de este framework es necesario tener un Axon Server arrancado.

Este proporciona una base de datos donde se guardan los registros del cambio de estado de cada uno de los aggreadores asi como también, automatización para comunicar los eventos y comandos al rededor de todos los microservicios conectados a este servidor.

Para arrancar un **Axon Server** en local, se puede hacer con el siguiente comando de docker: 

> docker run -t --name my-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

Debemos añadir la respectiva dependencia en el .pom de nuestros microservicios: 

```
<dependency>
    <groupId>org.axonframework</groupId> 
    <artifactId>axon-spring-boot-starter</artifactId> 
    <version>4.0.3</version>
</dependency>
``` 

En caso de querer utilizar Axon solo para estructurar una arquitectura CQRS sin EventSourcing y Sagas, se puede utilizar sin Axon Server. 

Este se ignora añadiendo esto a la dependencia en el .pom:

```
<exclusions>
    <exclusion> 
        <groupId>org.axonframework</groupId>
        <artifactId>axon-server-connector</artifactId> 
    </exclusion>
</exclusions>
```

[Axon Server](https://docs.axoniq.io/reference-guide/axon-server/introduction) es totalmente configurable y también se puede utilizar Kubernetes para configurar temas de seguridad, bases de datos, Network Policies, entre otros.