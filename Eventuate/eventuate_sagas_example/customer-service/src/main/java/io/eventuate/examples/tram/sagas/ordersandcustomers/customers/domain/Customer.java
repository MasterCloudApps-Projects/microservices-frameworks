package io.eventuate.examples.tram.sagas.ordersandcustomers.customers.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name="Customer")
@Access(AccessType.FIELD)
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  private Double creditLimit;

  @ElementCollection
  private Map<Long, Double> creditReservations;

  @Version
  private Long version;

  Double availableCredit() {
    return creditLimit - creditReservations.values().stream().reduce(0.0, Double::sum);
  }

  public Customer() {
    this.creditLimit = 0.0;
  }

  public Customer(String name, Double creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
    this.creditReservations = Collections.emptyMap();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getCreditLimit() {
    return creditLimit;
  }

  public void reserveCredit(Long orderId, Double orderTotal) {
    if (availableCredit() >= orderTotal) {
      creditReservations.put(orderId, orderTotal);
    } else
      throw new CustomerCreditLimitExceededException();
  }
}
