package com.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.repositories.StoreRepository;
import com.app.services.*;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PurchaseController {
	
	@Autowired
	private PurchaseService service;

	@PostMapping("/purchases")
	public @ResponseBody ResponseEntity<HttpStatus> createOne(@RequestBody PurchaseView view){
		return service.createPurchase(view);
	}
	
	@GetMapping("/purchases")
	public @ResponseBody PurchaseView getOne(@RequestParam("id") long id) throws EntityNotFoundException {
		return new PurchaseView(service.getPurchase(id));
	}
}
