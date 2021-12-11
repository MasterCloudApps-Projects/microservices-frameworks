# Estudio de frameworks de microservicios (Sagas)

## 👋 Introducción

Este repositorio consiste en el resultado de una investigación sobre el patrón Saga y la implementación de varios frameworks especializados en el uso de estas.

Hemos comparado como se implementan, la curva de aprendizaje, rapidez, antigüedad, comunidad, documentación, entre otras cosas.

Nuestros objetivos con esta investigación es: 
- **Investigar** las opciones de frameworks que hay en el mercado.
- **Generar una Documentación**, ya que carecen de información.
- **Comparar** cada una de estas tecnologías para resaltar los pros y cons, y ayudar a encontrar la opción que más se ajuste a las necesidad de cada caso de uso.

 📚 [Enlace a memoria](https://github.com/MasterCloudApps-Projects/microservices-frameworks/blob/main/docs/memoria.pdf)

## 📊 Comparativa y Ejercicio
Para poder hacer la comparación, entre las tecnologías que escogimos, hemos hecho el mismo ejercicio para cada uno de estos frameworks. 

El ejericio consta de dos servicios: 

 ### Servicios:

- **Order Service**: El servicio de las ordenes que se encarga de crearlas, ordenarlas y terminarlas. Aquí es donde se ejerce el patrón Saga para la comunicación con otros microservicios
- **Customer Service**: Este es un servicio para los customers que se encarga de verificar el balance y decir si se puede o no aceptar la orden. 

Consiste en crear una Saga que se encargue de comunicar el **Order Service** con el **Customer Service** al momento de crear una orden y que esta pase por los diferentes pasos y estados. 

### Pasos:

- **Crear Customer**: Tenemos que crear el customer, al que va asociada una o varias ordenes, con un balance especifico. 
- **Crear Orden**: Se crea una orden con su respectivo Customer.
- **Verificar Balance**: Se verifica desde el **Order Service** que el Customer al que va a asociada la orden tenga suficiente balance como para aceptar la orden. En caso de no tener suficiente balance para aceptar esta orden, el customer debe de rechazar la orden.
- **Actualizar Estado Final de la Orden**: En función de la respuesta del **Customer Service** se actualiza el estado de la orden, sea rechazada o aprobada.

## 👨‍💻 Frameworks

Los **frameworks** que hemos elegido son: 

## Axon:
<img src="./assets/img/axon.png" width="600"/>

<br/>

Es un framework desarrollado para la construcción de microservicios controlados por eventos, basado en las arquitecturas CQRS y Event Sourcing y para la implementación de Sagas entre estos.

Para ver más info de Axon Framework, [aquí](https://docs.axoniq.io/reference-guide/) la documentación.

[Ejemplo de Axon Framework](https://github.com/MasterCloudApps-Projects/microservices-frameworks/tree/main/Axon%20Framework) : Ejemplo simple de implementación de patrón Sagas en Axon Framework.


 ## Eventuate:
<img src="./assets/img/eventuate.jpg" width="350"/>

<br/>

Eventuate Tram Sagas es un framework para implementar sagas en microservicios Java que usen SpringBoot, Micronaut o Quarkus. Está basado en el framework Eventuate Tram, que funciona mediante el envío de mensajes asíncronos entre los distintos participantes de la saga. Esto permite a los microservicios acualizar de forma automática su estado y publicar esta información como mensajes o eventos a otros servicios.

Para ver más info de Eventuate, [aquí](https://eventuate.io/docs/manual/eventuate-tram/latest/getting-started-eventuate-tram.html) la documentación.

[Ejemplo de Eventuate Tram](https://github.com/MasterCloudApps-Projects/microservices-frameworks/tree/main/Eventuate) : Ejemplo simple de implementación de patrón Sagas en Eventuate Tram.

 ## Cadence:
<img src="./assets/img/cadence.png" width="350"/>

<br/>

Cadence consiste en un framework de programación (cliente) y un servicio de gestión (backend). El framework permite al desarrollador crear y coordinar funciones y soporta los lenguajes Go, Java, Python y Ruby (aunque los dos últimos no tienen soporte oficial). El backend es un servicio stateless y depende de un almacenamiento persistente, con Cassandra, MySQL o PostgreSQL. Este servicio backend maneja el historial de los workflows, coordina las actividades de cada participante, redirige las señales al worker correcto, etc..

Para ver más info de Cadence Workflow, [aquí](https://cadenceworkflow.io/docs/get-started/) la documentación.

[Ejemplo de Cadence](https://github.com/MasterCloudApps-Projects/microservices-frameworks/tree/main/Cadence) : Ejemplo simple de implementación de patrón Sagas en Cadence Workflow.


