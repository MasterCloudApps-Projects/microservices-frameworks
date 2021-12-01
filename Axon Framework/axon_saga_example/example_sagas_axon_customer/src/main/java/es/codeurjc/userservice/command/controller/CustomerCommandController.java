package es.codeurjc.userservice.command.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.userservice.command.service.CustomerCommandService;
import es.codeurjc.userservice.dto.CreateCustomerRequest;

@RestController
public class CustomerCommandController {

	@Autowired
	CustomerCommandService customerService;
	
	@PostMapping(value = "/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }
}
