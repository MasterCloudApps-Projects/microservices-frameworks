package es.codeurjc.userservice.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.userservice.query.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, String> {

}
