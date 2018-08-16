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
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class StoreController {
	
	@Autowired
	private StoreService service;

	@PostMapping("/stores")
	public @ResponseBody ResponseEntity<StoreView> createOne(@RequestBody StoreView view) throws InvalidInputDataException, EntityAlreadyExistsException{
		Store savedStore = service.createStore(view);
		StoreView savedStoreView = new StoreView(savedStore);
		return new ResponseEntity<StoreView>(savedStoreView, HttpStatus.CREATED);
	}
	
	@GetMapping("/stores/{id}")
	public @ResponseBody ResponseEntity<StoreView> getOne(@PathVariable("id") long id) throws EntityNotFoundException, InvalidInputDataException {
		StoreView sv = new StoreView(service.getStore(id));
		return new ResponseEntity<StoreView>(sv, HttpStatus.OK);

	}
	
	@GetMapping("/stores")
	public @ResponseBody ResponseEntity<List<StoreView>> getAll() {
		Iterable<Store> stores = service.getAllStores();
		List<StoreView> storeViews = new ArrayList<StoreView>();
		for (Store store : stores) {
		    storeViews.add(new StoreView(store));
		}
		return new ResponseEntity<List<StoreView>>(storeViews, HttpStatus.OK);

	}
}
