# Cadence
It supports Java Spring Boot, Go, Python and Ruby.

This framework has a package that allows us to define and create arquitectures based on events and Sagas.

Here we'll explain the basic documentation and how it can be used with Java.

## Documentation

The **Cadence** core is based on a unit that's called *workflow*, that is the equivalent to a Saga. A workflow is made by a series of steps and functions that allow us to do an orquestration of a process between several services.

The first step is to create the base of a workflow as a interface and defined there the methods that will be implemented.

This is made with the **@WorkflowMethod** annotation:
```
@WorkflowMethod
Long createOrder(Long customerId, Double totalMoney);
```
After defining the **Workflow**, we need to create it's implementation:

```
public class CreateOrderWorkflowImpl implements CreateOrderWorkflow {
```

The next step is to define the **Activities**. This are functions (async o sync) that are invoked throught out the steps of the workflow.

Like the Workflow, the activities need to be defined as an interface and then implementing it later inside a workflow.

For example, we create an interface:

```
public interface CustomerActivities {
	void reserveCredit(Long customerId, Double amount);
}
```

And implement it: 

```
public class OrderActivitiesImpl implements OrderActivities {
    @Override
    public Long createOrder (Long customerId, Double amount) {
        service.saveOrder(order);
        return order.getId();
    }
    .
    .
    .
}
```

After having implemented it, we need to the create an instance of it when initializing the workflow using an ActivityOptions Builder served by Cadence.

```
private final ActivityOptions orderActivityOptions = new ActivityOptions.Builder()
        .setTaskList("OrderTaskList")
        .setScheduleToCloseTimeout(Duration.ofSeconds(10))
        .build();
private final OrderActivities orderActivities =
            Workflow.newActivityStub(OrderActivities.class, orderActivityOptions);
```

After having the workflows and activities defined and implemented, we build the workflow and inside it we create the respective Saga. In order to do it we use a Builder that Cadence gives us:

```
Saga.Options sagaOptions = new Saga.Options.Builder().build();
Saga saga = new Saga(sagaOptions);
```
Then, we add the activities to the workflow used in their respective steps. Like this:
```
Long orderId = orderActivities.createOrder(customerId, amount);
saga.addCompensation(orderActivities::rejectOrder, orderId, rejectedReason);
customerActivities.reserveCredit(customerId, amount);
```
We can compensate inside of a Saga with the method saga.compensate() inside of a catch:

```
} catch (ActivityFailureException e) {
    if(e.getCause() != null && e.getCause().getCause() instanceof CustomerNotFoundException) {
        rejectedReason = "CUSTOMER NOT FOUND";
    } else {
        rejectedReason = "CREDIT LIMIT EXCEEDED";
    }
    saga.compensate();
    throw e;
}
```

At the begining of the app we define a @Bean where we can initialize a workflowClient to be available all along our application:

```
@Bean
WorkflowClient workflowClient() {
    IWorkflowService service = new WorkflowServiceTChannel(ClientOptions.defaultInstance());

    WorkflowClientOptions workflowClientOptions = WorkflowClientOptions.newBuilder()
            .setDomain("example")
            .build();
    return WorkflowClient.newInstance(service, workflowClientOptions);
}

@Bean
CommandLineRunner commandLineRunner(WorkflowClient workflowClient) {
    return args -> {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker("OrderTaskList");
        worker.registerWorkflowImplementationTypes(CreateOrderWorkflowImpl.class);
        worker.registerActivitiesImplementations(new OrderActivitiesImpl(service));
        factory.start();
    };
}
```

With thiis made we can initialized a workflowClient everytime we need to, for example:
```
public void save(Order order) {
    CreateOrderWorkflow workflow = workflowClient.newWorkflowStub(
            CreateOrderWorkflow.class,
            new WorkflowOptions.Builder()
                    .setExecutionStartToCloseTimeout(Duration.ofSeconds(10000))
                    .setTaskList("OrderTaskList")
                    .build()
    );
    WorkflowClient.execute(workflow::createOrder, order.getCustomerId(), order.getMoney());
}
```

## Implementation
Create the interfaces of the activities and workflows: 

```
public interface CustomerActivities {
	void reserveCredit(Long customerId, Double amount);
}
public class CustomerActivitiesImpl implements CustomerActivities {
```

```
public interface OrderActivities {
	Long createOrder(Long customerId, Double money);
    void approveOrder(Long orderId);
    void rejectOrder(Long orderId, String rejectionReason);
}
public class OrderActivitiesImpl implements OrderActivities {
```
And then implementing it:

```
public interface CreateOrderWorkflow {
	@WorkflowMethod
    Long createOrder(Long customerId, Double totalMoney);
}
public class CreateOrderWorkflowImpl implements CreateOrderWorkflow {
    @Override
    public Long createOrder(Long customerId, Double amount) {
        System.out.print("Entrando al principio de la funcion");
        Saga.Options sagaOptions = new Saga.Options.Builder().build();
```

Then we'll create a new Saga inside of the workflow and build the flow with the activities and compensations:

```
@Override
    public Long createOrder(Long customerId, Double amount) {
        System.out.print("Entrando al principio de la funcion");
        Saga.Options sagaOptions = new Saga.Options.Builder().build();
        Saga saga = new Saga(sagaOptions);
        System.out.print("Entro aca");
        String rejectedReason = "REJECTED_REASON";
        try {
            Long orderId = orderActivities.createOrder(customerId, amount);
            System.out.print("Entro aca");
            saga.addCompensation(orderActivities::rejectOrder, orderId, rejectedReason);
            customerActivities.reserveCredit(customerId, amount);
            orderActivities.approveOrder(orderId);
            System.out.print("Entro aca");
            return orderId;
        } catch (ActivityFailureException e) {
            if(e.getCause() != null && e.getCause().getCause() instanceof CustomerNotFoundException) {
                rejectedReason = "CUSTOMER NOT FOUND";
            } else {
                rejectedReason = "CREDIT LIMIT EXCEEDED";
            }
            saga.compensate();
            throw e;
        }
    }
```
## Compile and launch our application
These are the steps to launch the cadence backend need it to work:

### Download docker compose Cadence Server
Download the docker-compose file neeed to initialize our backend and run it.

> curl -O https://raw.githubusercontent.com/uber/cadence/master/docker/docker-compose.yml
> 
> docker-compose up

### Run cadence server host
**OPTIONAL**

This a server host (Graphs and Data) that allows to see how the steps and workflows are being executed through out our application:
> docker run --network=host --rm ubercadence/cli:master --do example domain register -rd 1

## Use cases
To try this application we have this calls and URLs:

**Create a customer POST (http://localhost:8081/customers)**:

body:
```
{
    "name": "Stefano Lagattolla",
    "money": 18000
}
```
**Create an order POST (http://localhost:8080/orders)**:

body:
```
{
    "money": 1500,
    "customerId": "1"
}
```
**Get an order GET (http://localhost:8080/orders)**:

body:
```
[
    {
        "id": orderid,
        "state": "APPROVED"
        "money": 1500,
        "customerId": "3ce57fdf-a5d0-468d-8f42-6dea737819e52",
        "rejectedReason": ""
    },
    ...
]
```

**Get a customer GET (http://localhost:8081/get_customer?id=customerid)**:

body:
```
{
    "id": customerid,
    "name": "Stefano",
    "money": 1500
}
```
