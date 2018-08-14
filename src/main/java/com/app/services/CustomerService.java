package com.app.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;
import com.app.utilities.CustomerNotFoundException;

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
	
	public Customer getCustomer(long id) throws CustomerNotFoundException {
		Optional<Customer> _customer = repository.findById(id);
		if (!_customer.isPresent()) {
            throw new CustomerNotFoundException(id);
        }
        return _customer.get();
    }
	
	public void changeMoney(long id, double money) {
		Optional<Customer> _customer = repository.findById(id);
		if (!_customer.isPresent()) {
			String message = "Customer with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        Customer customer = _customer.get();
        customer.setBalance(money);
        repository.save(customer);
	}
	
	public List<Purchase> getPurchases(long id){
		Optional<Customer> _customer = repository.findById(id);
		if (!_customer.isPresent()) {
			String message = "Book with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        Customer customer = _customer.get();
        return customer.getPurchases();
	}
}