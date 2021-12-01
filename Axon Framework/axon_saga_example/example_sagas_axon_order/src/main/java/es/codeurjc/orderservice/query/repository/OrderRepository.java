package es.codeurjc.orderservice.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.orderservice.query.entity.Order;


public interface OrderRepository extends JpaRepository<Order, String> {

}
