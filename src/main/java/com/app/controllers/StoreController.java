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
public class StoreController {
	
	@Autowired
	private StoreService service;

	@PostMapping("/stores")
	public @ResponseBody ResponseEntity<StoreView> createOne(@RequestBody StoreView view){
		Store receivedStore = new Store(view);
		Store savedStore = service.createStore(receivedStore);
		StoreView savedStoreView = new StoreView(savedStore);
		return new ResponseEntity<StoreView>(savedStoreView, HttpStatus.CREATED);
	}
	
	@GetMapping("/stores")
	public @ResponseBody StoreView getOne(@RequestParam("id") long id) throws EntityNotFoundException {
		return new StoreView(service.getStore(id));
	}
	
	@GetMapping("/stores/all")
	public @ResponseBody List<StoreView> getAll() {
		Iterable<Store> stores = service.getAllStores();
		List<StoreView> storeViews = new ArrayList<StoreView>();
		for (Store store : stores) {
		    storeViews.add(new StoreView(store));
		}
		return storeViews;
	}
	
//	@DeleteMapping("/stores")
//	public @ResponseBody ResponseEntity<HttpStatus> deleteOne(@RequestParam("id") long id) throws EntityNotFoundException {
//		return service.deleteStore(id);
//	}
//	
//	@PutMapping("/stores")
//	public @ResponseBody ResponseEntity<HttpStatus> updateOne(@RequestBody Store store) throws EntityNotFoundException {
//		return service.updateStore(store);
//	}
}
