package com.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.models.*;
import com.app.repositories.*;
import com.app.services.*;
import com.app.utilities.*;
import com.app.views.*;

@RestController
public class StatisticsController {
	
	long STORE_ID = 56;
	
	@Autowired
	private StoreRepository sRepository;
	
	@Autowired
	private BookRepository bRepository;
	
	@Autowired
	private CustomerRepository cRepository;
	
	@Autowired
	private PurchaseRepository pRepository;
	
	@GetMapping("/statistics/all")
	public @ResponseBody ResponseEntity<StatisticsView> getAllStatistics(){
		StatisticsView view = new StatisticsView();
		Optional<Store> _store = sRepository.findById(STORE_ID);
		if(_store.isPresent()) {
			view.store =  new StoreView(_store.get());
		}
		List<Book> books = bRepository.findAll();
		List<BookViewWithAmount> bookViews = new ArrayList<BookViewWithAmount>();
		for(Book book: books) {
			BookViewWithAmount bview = new BookViewWithAmount();
			bview.id = book.getId();
			bview.title = book.getTitle();
			if(book.getWarehouse()!=null) {
				bview.amount = book.getWarehouse().getAmount();
			}
			else {
				bview.amount=0;
			}
			if(book.getWarehouse()!=null) {
				bview.price = book.getWarehouse().getPrice();
			}
			else {
				bview.price=0D;
			}
			bookViews.add(bview);
		}
		view.books = bookViews;
		List<Customer> customers = cRepository.findAll();
		List<CustomerView> customerViews = new ArrayList<CustomerView>();
		for(Customer c: customers) {
			CustomerView cv = new CustomerView();
			cv.id = c.getId();
			cv.name = c.getName();
			cv.balance = c.getBalance();
			customerViews.add(cv);
		}
		view.customers = customerViews;
		List<Purchase> purchases = pRepository.findAll();
		List<PurchaseView> purchaseViews = new ArrayList<PurchaseView>();
		for(Purchase p: purchases) {
			PurchaseView pv = new PurchaseView(p);
			purchaseViews.add(pv);
		}
		view.purchases = purchaseViews;
		return new ResponseEntity<StatisticsView>(view,HttpStatus.OK);
	}

	@GetMapping("/statistics/books")
	public @ResponseBody ResponseEntity<List<BookViewWithAmount>> bookStatistics(){
		List<Book> books = bRepository.findAll();
		List<BookViewWithAmount> bookViews = new ArrayList<BookViewWithAmount>();
		for(Book book: books) {
			BookViewWithAmount bview = new BookViewWithAmount();
			bview.id = book.getId();
			bview.title = book.getTitle();
			if(book.getWarehouse()!=null) {
				bview.amount = book.getWarehouse().getAmount();
			}
			else {
				bview.amount=0;
			}
			if(book.getWarehouse()!=null) {
				bview.price = book.getWarehouse().getPrice();
			}
			else {
				bview.price=0D;
			}
			bookViews.add(bview);
		}
		return new ResponseEntity<List<BookViewWithAmount>>(bookViews,HttpStatus.OK);
	}
}

