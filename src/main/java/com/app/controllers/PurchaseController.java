package com.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.repositories.StoreRepository;
import com.app.services.*;
import com.app.utilities.BookNotAvailableException;
import com.app.utilities.CustomerNotEnoughMoneyException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.utilities.InvalidPurchaseStatusException;
import com.app.utilities.PurchaseNotFoundException;
import com.app.utilities.StoreNotEnoughMoneyException;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PurchaseController {
	
	@Autowired
	private PurchaseService service;

	@PostMapping("/purchases")
	public @ResponseBody ResponseEntity<PurchaseView> createOne(@RequestBody PurchaseView view) throws InvalidInputDataException, EntityNotFoundException, BookNotAvailableException, CustomerNotEnoughMoneyException {
		PurchaseView pv = new PurchaseView(service.createPurchase(view));
		return new ResponseEntity<PurchaseView>(pv, HttpStatus.CREATED);
	}
	
	@PutMapping("/purchases/{id}/pay")
	public @ResponseBody ResponseEntity<HttpStatus> payOne(@PathVariable("id") long id) throws EntityNotFoundException, PurchaseNotFoundException, BookNotAvailableException, CustomerNotEnoughMoneyException, InvalidPurchaseStatusException{
		service.payPurchase(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	@PutMapping("/purchases/{id}/cancel")
	public @ResponseBody ResponseEntity<HttpStatus> cancelOne(@PathVariable("id") long id) throws EntityNotFoundException, StoreNotEnoughMoneyException, InvalidPurchaseStatusException{
		service.cancelPurchase(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	@GetMapping("/purchases/{id}")
	public @ResponseBody PurchaseView getOne(@PathVariable("id") long id) throws EntityNotFoundException {
		return new PurchaseView(service.getPurchase(id));
	}
	
}
