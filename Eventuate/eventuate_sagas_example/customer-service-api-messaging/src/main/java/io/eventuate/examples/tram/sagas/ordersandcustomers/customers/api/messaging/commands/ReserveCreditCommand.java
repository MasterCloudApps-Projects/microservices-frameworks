package io.eventuate.examples.tram.sagas.ordersandcustomers.customers.api.messaging.commands;

import io.eventuate.tram.commands.common.Command;

public class ReserveCreditCommand implements Command {
  private Long orderId;
  private Double orderTotal;
  private long customerId;

  public ReserveCreditCommand() {
    this.orderTotal = 0.0;
  }

  public ReserveCreditCommand(Long customerId, Long orderId, Double orderTotal) {
    this.customerId = customerId;
    this.orderId = orderId;
    this.orderTotal = orderTotal;
  }

  public Double getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(Double orderTotal) {
    this.orderTotal = orderTotal;
  }

  public Long getOrderId() {

    return orderId;
  }

  public void setOrderId(Long orderId) {

    this.orderId = orderId;
  }

  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }
}
