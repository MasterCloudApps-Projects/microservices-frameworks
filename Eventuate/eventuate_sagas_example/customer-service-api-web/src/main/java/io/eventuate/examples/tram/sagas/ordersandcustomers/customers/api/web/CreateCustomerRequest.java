package io.eventuate.examples.tram.sagas.ordersandcustomers.customers.api.web;

public class CreateCustomerRequest {
  private String name;
  private Double creditLimit;

  public CreateCustomerRequest() {
    this.creditLimit = 0.0;
  }

  public CreateCustomerRequest(String name, Double creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public Double getCreditLimit() {
    return creditLimit;
  }
}
