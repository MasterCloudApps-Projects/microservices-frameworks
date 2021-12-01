package io.eventuate.examples.tram.sagas.ordersandcustomers.orders.api.messaging.common;

import javax.persistence.Embeddable;

@Embeddable
public class OrderDetails {

  private Long customerId;

  private Double orderTotal;

  public OrderDetails() {
  }

  public OrderDetails(Long customerId, Double orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public Double getOrderTotal() {
    return orderTotal;
  }
}
