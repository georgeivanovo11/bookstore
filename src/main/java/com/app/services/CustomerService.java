package com.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.ACCEPTED;



@Service
public class CustomerService {
	
	@Autowired
    private CustomerRepository repository;
	
	public Customer createCustomer(CustomerView view) throws EntityAlreadyExistsException, InvalidInputDataException {
		if(view.name==null) {
			 throw new InvalidInputDataException("name");
		}
		if(view.balance==null) {
			 throw new InvalidInputDataException("balance");
		}
		if(view.id != null && repository.existsById(view.id)) {
			throw new EntityAlreadyExistsException("customer", view.id);
		}
		Customer customer = new Customer(view.id,view.name,view.balance);
        return repository.save(customer);
    }
	
	public Customer getCustomer(Long id) throws EntityNotFoundException, InvalidInputDataException  {
		Optional<Customer> customer = repository.findById(id);
		if (!customer.isPresent()) {
            throw new EntityNotFoundException("customer",id);
        }
        return customer.get();
    }
	
	public List<Customer> getAllCustomers() {
		List<Customer> customers = repository.findAll();
        return customers;
    }
	
	public void changeMoney(Long id, Double balance) throws EntityNotFoundException, InvalidInputDataException {
		if(id==null) {
			 throw new InvalidInputDataException("id");
		 }
		if(balance==null) {
			 throw new InvalidInputDataException("balance");
		 }
		Optional<Customer> _customer = repository.findById(id);
		if (!_customer.isPresent()) {
            throw new EntityNotFoundException("customer",id);
        }
        Customer customer = _customer.get();
        customer.setBalance(balance);
        repository.save(customer);
	}
	
	public List<Purchase> getPurchasesOfTheCustomer(Long id) throws EntityNotFoundException, InvalidInputDataException{
		Optional<Customer> _customer = repository.findById(id);
		if (!_customer.isPresent()) {
            throw new EntityNotFoundException("book", id);
        }
        Customer customer = _customer.get();
        return customer.getPurchases();
	}
	
	public void deleteAllCustomers(){
		this.repository.deleteAll();
	}
}