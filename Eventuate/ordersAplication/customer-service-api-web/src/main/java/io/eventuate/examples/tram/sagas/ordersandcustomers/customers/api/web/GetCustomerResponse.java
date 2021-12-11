package io.eventuate.examples.tram.sagas.ordersandcustomers.customers.api.web;


public class GetCustomerResponse {
  private Long customerId;
  private String name;
  private Double creditLimit;

  public GetCustomerResponse() {
  }

  public GetCustomerResponse(Long customerId, String name, Double creditLimit) {
    this.customerId = customerId;
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(Double creditLimit) {
    this.creditLimit = creditLimit;
  }
}
