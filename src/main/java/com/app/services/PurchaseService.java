package com.app.services;

import java.util.ArrayList;
import java.util.List;
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
public class PurchaseService {
	
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private PurchaseRepository purchaseRepository;
	
	@Autowired
    private BookRepository bookRepository;

	
	public ResponseEntity<HttpStatus> createPurchase(PurchaseView view) {
		Optional<Customer> _customer = customerRepository.findById(view.customer_id);
		if (!_customer.isPresent()) {
			String message = "Customer with id: " + view.customer_id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
		Customer customer = _customer.get();
		
		Purchase purchase = new Purchase("pending");
		purchase.setCustomer(customer);
		
		List<Long> notFoundArray = new ArrayList<Long>();
		for(long item: view.books) {
			Optional<Book> _book = bookRepository.findById(item);
			if (!_book.isPresent()) {
				notFoundArray.add(item);
				continue;
	        }
			Book book = _book.get();
			int amount = book.getWarehouse().getAmount();
			if(amount<=0) {
				notFoundArray.add(item);
				continue;
			}
			book.getWarehouse().setAmount(amount-1);
			Purchasebook pb = new Purchasebook(book,purchase,1);
			book.getPurchasebooks().add(pb);
			purchaseRepository.save(purchase);
			bookRepository.save(book);	

		}
		purchase.setTotalPayment(100);
		purchaseRepository.save(purchase);
		return new ResponseEntity<>(CREATED);
    }
	
	public Purchase getPurchase(long id) {
		Optional<Purchase> purchase = purchaseRepository.findById(id);
		if (!purchase.isPresent()) {
			String message = "Purchase with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        return purchase.get();
    }

}

