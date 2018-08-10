package com.app.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;
import com.app.views.*;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService service;

	@PostMapping("/customers")
	public @ResponseBody ResponseEntity<HttpStatus> createOne(@RequestBody CustomerView view){
		Customer customer = new Customer(view);
		return service.createCustomer(customer);
	}
	
	@GetMapping("/customers/all")
	public @ResponseBody List<CustomerView> getAll() {
		Iterable<Customer> customers = service.getAllCustomers();
		List<CustomerView> customerViews = new ArrayList<CustomerView>();
		for (Customer customer : customers) {
			customerViews.add(new CustomerView(customer));
		}
		return customerViews;
	}
	
}