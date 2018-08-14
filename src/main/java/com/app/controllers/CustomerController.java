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
import com.app.utilities.CustomerNotFoundException;
import com.app.views.*;

@RestController
public class CustomerController {
	
	int i=0;
	
	@Autowired
	private CustomerService service;

	@PostMapping("/customers")
	public @ResponseBody ResponseEntity<HttpStatus> createOne(@RequestBody CustomerView view){
		Customer customer = new Customer(view);
		return service.createCustomer(customer);
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
	
	@GetMapping("/customers/{id}")
	public @ResponseBody ResponseEntity<CustomerView> getOne(@PathVariable("id") long id) throws CustomerNotFoundException {
		Customer customer = service.getCustomer(id);
		CustomerView cv = new CustomerView(customer);
		return new ResponseEntity<CustomerView>(cv, HttpStatus.OK);
	}
	
	@PutMapping("/customers/{id}/money")
	public @ResponseBody ResponseEntity<HttpStatus> updateOne(@PathVariable("id") long id, @RequestBody CustomerView view) throws EntityNotFoundException {
		service.changeMoney(id, view.balance);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	
	@GetMapping("/customers/{id}/purchases")
	public @ResponseBody List<PurchaseView> getAllPurchases(@PathVariable("id") long id) throws EntityNotFoundException {
		List<Purchase> purchases = service.getPurchases(id);
		List<PurchaseView> purchaseViews = new ArrayList<PurchaseView>();
		for (Purchase p : purchases) {
			PurchaseView pv = new PurchaseView(p);
			purchaseViews.add(pv);
		}
		return purchaseViews;
	}
	
}