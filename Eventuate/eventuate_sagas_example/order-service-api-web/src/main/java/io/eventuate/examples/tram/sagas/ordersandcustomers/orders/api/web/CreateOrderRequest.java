package io.eventuate.examples.tram.sagas.ordersandcustomers.orders.api.web;


public class CreateOrderRequest {
  private Double orderTotal;
  private Long customerId;

  public CreateOrderRequest() {
    this.orderTotal = 0.0;
  }

  public CreateOrderRequest(Long customerId, Double orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public Double getOrderTotal() {
    return orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }
}
