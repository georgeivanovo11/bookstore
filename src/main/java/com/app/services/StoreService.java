package com.app.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
public class StoreService {
	
	@Autowired
    private StoreRepository repository;
	
	public ResponseEntity<HttpStatus> createStore(Store store) {
		if(repository.existsById(store.id)) {
			store.id=0;
		}
        repository.save(store);
        return new ResponseEntity<>(CREATED);

    }
	
	public Store getStore(Long id) throws EntityNotFoundException {
		Optional<Store> store = repository.findById(id);
		if (!store.isPresent()) {
			String message = "Store with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        return store.get();
    }
	
	public Iterable<Store> getAllStores() {
		Iterable<Store> stores = repository.findAll();
        return stores;
    }
	
	public ResponseEntity<HttpStatus> deleteStore(Long id) throws EntityNotFoundException {
		Optional<Store> store = repository.findById(id);
		if (!store.isPresent()) {
			String message = "Store with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
		repository.delete(store.get());
        return new ResponseEntity<>(NO_CONTENT);
	}
	
	public ResponseEntity<HttpStatus> updateStore(Store store) {
		if(!repository.existsById(store.id)) {
			String message = "Store with id: " + store.id + " doesn't exist!";
			throw new EntityNotFoundException(message);
		}
		repository.save(store);
        return new ResponseEntity<>(ACCEPTED);
	}
}

