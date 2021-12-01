
# Eventuate Tram Sagas Ampliado: Products, Customers y Orders

### Compilar y ejecutar

Compilamos la aplicación y levantamos los servicios mediante DockerCompose

```
    ./gradlew assemble

    ./gradlew mysqlComposeBuild
    ./gradlew mysqlComposeUp
```

Las urls disponibles para probar estos servicios son:

    - order-service:  http://localhost:8081
    - customer-service:  http://localhost:8082
    - api-gateway:  http://localhost:8083


### Parar la aplicación

Para y elimina los contenedores creados por el docker-compose

```
    $ ./gradlew mysqlComposeDown
```