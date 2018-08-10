package com.app.services;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;
import com.app.views.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
public class StoreService {
	
	@Autowired
    private StoreRepository storeRepository;
	
	@Autowired
    private BookRepository bookRepository;

	
	public Store createStore(Store store) {
		if(storeRepository.existsById(store.getId())) {
			store.setId(0);
		}
		Store savedStore = storeRepository.save(store);
        return savedStore;
    }
	
	public Store getStore(Long id) throws EntityNotFoundException {
		Optional<Store> store = storeRepository.findById(id);
		if (!store.isPresent()) {
			String message = "Store with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        return store.get();
    }
	
	public Iterable<Store> getAllStores() {
		Iterable<Store> stores = storeRepository.findAll();
        return stores;
    }
	
//	public ResponseEntity<HttpStatus> deleteStore(Long id) throws EntityNotFoundException {
//		Optional<Store> store = storeRepository.findById(id);
//		if (!store.isPresent()) {
//			String message = "Store with id: " + id + " doesn't exist!";
//            throw new EntityNotFoundException(message);
//        }
//		storeRepository.delete(store.get());
//        return new ResponseEntity<>(NO_CONTENT);
//	}
//	
//	public ResponseEntity<HttpStatus> updateStore(Store store) {
//		if(!storeRepository.existsById(store.getId())) {
//			String message = "Store with id: " + store.getId()+ " doesn't exist!";
//			throw new EntityNotFoundException(message);
//		}
//		storeRepository.save(store);
//        return new ResponseEntity<>(ACCEPTED);
//	}
//	
//	public ResponseEntity<HttpStatus> createDelivery(DeliveryView view) {
////	    Optional<Store> _store = storeRepository.findById(view.store);
////	    if (!_store.isPresent()) {
////			String message = "Store with id: " + view.store + " doesn't exist!";
////            throw new EntityNotFoundException(message);
////        }
////	    Store store = _store.get();
////		for (BookItemView item : view.books) {
////			Optional<Book> _book = bookRepository.findById(item.id);
////			if (!_book.isPresent()) {
////				String message = "Book with id: " + item.id + " doesn't exist!";
////	            throw new EntityNotFoundException(message);
////	        }
////			Book book = _book.get();
////	        BookStore bookStore = new BookStore(store,book,item.amount, item.price);
////	        store.getBookStores().add(bookStore);
////		}
////		storeRepository.save(store);
////        return new ResponseEntity<>(ACCEPTED);
//	}
}

