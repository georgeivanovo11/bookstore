package com.app.controllers;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;

@RestController
public class StoreController {
	
	@Autowired
	private StoreService service;

	@PostMapping("/stores")
	public @ResponseBody ResponseEntity<HttpStatus> createOne(@RequestBody Store store){
		return service.createStore(store);
	}
	
	@GetMapping("/stores")
	public @ResponseBody Store getOne(@RequestParam("id") long id) throws EntityNotFoundException {
		return service.getStore(id);
	}
	
	@GetMapping("/stores/all")
	public @ResponseBody Iterable<Store> getAll() {
		return service.getAllStores();
	}
	
	@DeleteMapping("/stores")
	public @ResponseBody ResponseEntity<HttpStatus> deleteOne(@RequestParam("id") long id) throws EntityNotFoundException {
		return service.deleteStore(id);
	}
	
	@PutMapping("/stores")
	public @ResponseBody ResponseEntity<HttpStatus> updateOne(@RequestBody Store store) throws EntityNotFoundException {
		return service.updateStore(store);
	}
}
