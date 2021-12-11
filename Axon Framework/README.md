# Axon Framework
Es compatible y configurable con Java y Spring Boot<br>
Este **framework** tiene un paquete que te permite definir y crear arquitectura CQRS de manera rápida y sencilla.<br>
Acá explicaremos un poco la documentación y como hemos implementado esta en el ejemplo genérico.

## Documentación
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

[Axon Server](https://docs.axoniq.io/reference-guide/axon-server/introduction) es totalmente configurable y también se puede utilizar Kubernetes para configurar temas de seguridad, bases de datos, Network Policies, entre otros.

## Implementación

Primero que todo creamos los agregadores de **Customer** y **Order** para que estos se persistan. 

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

Luego creamos una Saga, que hemos decidido llamar **OrderSaga**.

Con esto definimos el flujo de como el proceso de creación y verificación de una orden va a ser. 

Definimos el inicio de la Saga, que será cuando se cree una orden y se disparé un evento especifico (orderCreatedEvent).
```
@StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
```

Se crea la estructura de la saga definiendo los eventos que vamos a recibir y los comandos que vamos a enviar para comunicar los dos servicios entre sí, entre ellos tenemos: OrderCreatedEvent, ValidatedCustomerPaymentEvent,InsufficientMoneyEvent, OrderRejectedCommand, OrderApprovedCommand.

Con todo esto tenemos definido toda la estructura y lo que tenemos que hacer es arrancar la app de esta manera: 

### Arrancamos el Axon Server
Esto es necesario para automatizar la persistencia de estados de los agregadores y la comunicación mediante EventSourcing de los servicios.

Para arrancar el servidor de Axon hay que correr lo siguiente: 
> docker run -t --name my-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver

### Arrancamos los dos servicios (Order y Customer)
Luego de arrancar el servidor es momento de arrancar cada servicio de manera individual
> /order mvn spring-boot:run
> /customer mvn spring-boot:run

### REST API
Para poder probar la aplicación tenemos las siguiente peticiones de POSTMAN: 

**Crear un customer POST (http://localhost:8081/create)**:
```
{
    "name": "Stefano Lagattolla",
    "balance": 18000
}
```
**Crear una orden POST (http://localhost:8080/order)**:
```
{
    "price": 1500,
    "customerId": "3ce57fdf-a5d0-468d-8f42-6dea737819e52"
}
```
**Obtener una orden GET (http://localhost:8080/get_order?id=orderid)**

```
{
    "id": orderid,
    "state": "APPROVED"
    "price": 1500,
    "customerId": "3ce57fdf-a5d0-468d-8f42-6dea737819e52",
    "rejectedReason": ""
    
}
```

**Obtener un customer GET (http://localhost:8081/get_customer?id=customerid)**

```
{
    "id": customerid,
    "name": "Stefano",
    "balance": 1500
}
```


