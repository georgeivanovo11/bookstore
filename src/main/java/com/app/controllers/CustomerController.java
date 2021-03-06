package com.app.controllers;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;

@RestController
public class CustomerController {
	
	int i=0;
	
	@Autowired
	private CustomerService service;

	@PostMapping("/customers")
	public @ResponseBody ResponseEntity<CustomerView> createOne(@RequestBody CustomerView view) throws EntityAlreadyExistsException, InvalidInputDataException{
		Customer savedCustomer =  service.createCustomer(view);
		CustomerView savedCustomerView = new CustomerView(savedCustomer);
		return new ResponseEntity<CustomerView>(savedCustomerView, HttpStatus.CREATED);
	}
	
	@GetMapping("/customers/{id}")
	public @ResponseBody ResponseEntity<CustomerView> getOne(@PathVariable("id") long id) throws EntityNotFoundException, InvalidInputDataException {
		Customer customer = service.getCustomer(id);
		CustomerView cv = new CustomerView(customer);
		return new ResponseEntity<CustomerView>(cv, HttpStatus.OK);
	}
	
	@GetMapping("/customers")
	public @ResponseBody List<CustomerView> getAll() {
		Iterable<Customer> customers = service.getAllCustomers();
		List<CustomerView> customerViews = new ArrayList<CustomerView>();
		for (Customer customer : customers) {
			customerViews.add(new CustomerView(customer));
		}
		return customerViews;
	}
	
	
	@PutMapping("/customers/{id}/money")
	public @ResponseBody ResponseEntity<HttpStatus> updateOne(@PathVariable("id") long id, @RequestBody BalanceView view) throws EntityNotFoundException, com.app.utilities.EntityNotFoundException, InvalidInputDataException {
		service.changeMoney(id, view.balance);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	
	@GetMapping("/customers/{id}/purchases")
	public @ResponseBody ResponseEntity<List<PurchaseView>> getAllPurchases(@PathVariable("id") long id) throws EntityNotFoundException, com.app.utilities.EntityNotFoundException, InvalidInputDataException {
		List<Purchase> purchases = service.getPurchasesOfTheCustomer(id);
		List<PurchaseView> purchaseViews = new ArrayList<PurchaseView>();
		for (Purchase p : purchases) {
			PurchaseView pv = new PurchaseView(p);
			purchaseViews.add(pv);
		}
		return new ResponseEntity<List<PurchaseView>>(purchaseViews, HttpStatus.OK);
	}
	
}