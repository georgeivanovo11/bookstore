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
	
	long STORE_ID = 56;
	int i;
	
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private PurchaseRepository purchaseRepository;
	
	@Autowired
    private BookRepository bookRepository;

	@Autowired
    private StoreRepository storeRepository;
	
	public long createPurchase(PurchaseView view) {
		
		//setup
		boolean isError=false;
		List<Long> notExistArray = new ArrayList<Long>();
		List<Long> notEnoughItemsArray = new ArrayList<Long>();
		boolean isEnoughMoney = true;
		boolean isCustomerExists = true;
		double totalPrice=0;
		
		//check		
		Optional<Customer> _customer = customerRepository.findById(view.customer_id);
		if (!_customer.isPresent()) {
			isCustomerExists = false;
        }
		
		for(long item: view.books) {
			Optional<Book> _book = bookRepository.findById(item);
			if (!_book.isPresent()) {
				notExistArray.add(item);
				isError=true;
				continue;
	        }
			Book book = _book.get();
			int amount = book.getWarehouse().getAmount();
			if(amount<=0) {
				notEnoughItemsArray.add(item);
				isError=true;
				continue;
			}
			totalPrice += book.getWarehouse().getPrice();
		}
		
		if(_customer.get().getBalance() - totalPrice <0 ) {
			isEnoughMoney=false;
			isError = true;
		}
		
		if(isError) {
			String message = "error";
            throw new EntityNotFoundException();
		}
		
		//act
		Customer customer = _customer.get();
		Purchase purchase = new Purchase("pending");
		purchase.setCustomer(customer);
		
		for(long item: view.books) {
			Optional<Book> _book = bookRepository.findById(item);
			Book book = _book.get();
			int amount = book.getWarehouse().getAmount();
			double price = book.getWarehouse().getPrice();
			book.getWarehouse().setAmount(amount-1);
			Purchasebook pb = new Purchasebook(book,purchase,1);
			book.getPurchasebooks().add(pb);
			purchaseRepository.save(purchase);
			bookRepository.save(book);	

		}
		purchase.setTotalPayment(totalPrice);
		Purchase saved = purchaseRepository.save(purchase);

		customer.setBalance(customer.getBalance() - totalPrice);
		customerRepository.save(customer);
		
		Optional<Store> _store = storeRepository.findById(STORE_ID);
		if(_store.isPresent()) {
			Store store = _store.get();
			store.setBalance(store.getBalance() + totalPrice);
			storeRepository.save(store);
		}
		
		return saved.getId();
    }
	
	public Purchase getPurchase(long id) {
		Optional<Purchase> purchase = purchaseRepository.findById(id);
		if (!purchase.isPresent()) {
			String message = "Purchase with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        return purchase.get();
    }
	
	public StatisticsView getStatistics() {
		StatisticsView view = new StatisticsView();
		Optional<Store> _store = storeRepository.findById(STORE_ID);
		if(_store.isPresent()) {
			view.store =  new StoreView(_store.get());
		}
		List<Book> books = bookRepository.findAll();
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
				bview.price=0;
			}
			bookViews.add(bview);
		}
		view.books = bookViews;
		List<Customer> customers = customerRepository.findAll();
		List<CustomerView> customerViews = new ArrayList<CustomerView>();
		for(Customer c: customers) {
			CustomerView cv = new CustomerView();
			cv.id = c.getId();
			cv.name = c.getName();
			cv.balance = c.getBalance();
			customerViews.add(cv);
		}
		view.customers = customerViews;
		List<Purchase> purchases = purchaseRepository.findAll();
		List<PurchaseView> purchaseViews = new ArrayList<PurchaseView>();
		for(Purchase p: purchases) {
			PurchaseView pv = new PurchaseView();
			pv.id = p.getId();
			pv.status = p.getSatus();
			pv.customer_id = p.getCustomer().getId();
			pv.totalPayment = p.getTotalPayment();
			pv.books = new long [p.getPurchasebooks().size()];
			i=0;
			p.getPurchasebooks().forEach(item -> {
				pv.books[i]=item.getBook().getId();
				i++;
			});
			purchaseViews.add(pv);
		}
		view.purchases = purchaseViews;
		return view;
	}

}

