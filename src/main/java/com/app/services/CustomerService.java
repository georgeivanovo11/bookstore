package com.app.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.ACCEPTED;



@Service
public class CustomerService {
	
	@Autowired
    private CustomerRepository repository;
	
	public ResponseEntity<HttpStatus> createCustomer(Customer customer) {
		if(repository.existsById(customer.getId())) {
			customer.setId(0);
		}
        repository.save(customer);
        return new ResponseEntity<>(CREATED);

    }
	
	public Iterable<Customer> getAllCustomers() {
		Iterable<Customer> customers = repository.findAll();
        return customers;
    }
}