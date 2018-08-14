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
import com.app.utilities.*;
import com.app.views.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Service
public class PurchaseService {
	
	long STORE_ID = 56;
	
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private WarehouseRepository warehouseRepository;
	
	@Autowired
    private PurchaseRepository purchaseRepository;
	
	@Autowired
    private BookRepository bookRepository;

	@Autowired
    private StoreRepository storeRepository;
	
	public Purchase getPurchase(long id) {
		Optional<Purchase> purchase = purchaseRepository.findById(id);
		if (!purchase.isPresent()) {
			String message = "Purchase with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        return purchase.get();
    }
	
	public Purchase createPurchase(PurchaseView view) throws CustomerNotFoundException, BookNotFoundException, BookNotAvailableException, CustomerNotEnoughMoneyException, PurchaseNotFoundException {
				
		//----------------------------check---------------------------------------		
		Optional<Customer> _customer = customerRepository.findById(view.customer_id);
		if (!_customer.isPresent()) {
			throw new CustomerNotFoundException(view.customer_id);
        }
		Customer customer = _customer.get();
		
		List<Long> notAvailableList = new ArrayList<Long>();
		double totalPayment = 0;
		for(long id: view.books) {
			Optional<Book> _book = bookRepository.findById(id);
			if (!_book.isPresent()) {
				throw new BookNotFoundException(id);
	        }
			Book book = _book.get();
			int amount = book.getWarehouse().getAmount();
			if(amount<=0) {
				notAvailableList.add(id);
			}
			totalPayment += book.getWarehouse().getPrice();
		}
		
		if(notAvailableList.size()>0) {
			throw new BookNotAvailableException(notAvailableList);
		}
		
		if(customer.getBalance() - totalPayment < 0 ) {
			throw new CustomerNotEnoughMoneyException(view.customer_id);
		}
	
		//-----------------------------act-------------------------------------------
		Purchase purchase = new Purchase("pending");
		purchase.setCustomer(customer);
		
		for(long id: view.books) {
			Book book = bookRepository.findById(id).get();
			Purchasebook pb = new Purchasebook(book,purchase);
			book.getPurchasebooks().add(pb);
			purchaseRepository.save(purchase);
			bookRepository.save(book);	
		}
		purchase.setTotalPayment(totalPayment);
		Purchase saved = purchaseRepository.save(purchase);
		
		return saved;
    }
	
	public void payPurchase(long id) throws PurchaseNotFoundException, PurchaseAlreadyPaidException, BookNotAvailableException, CustomerNotEnoughMoneyException {
		Optional<Purchase> _purchase = purchaseRepository.findById(id);
		if (!_purchase.isPresent()) {
            throw new PurchaseNotFoundException(id);
        }
		Purchase purchase = _purchase.get();
		Customer customer = purchase.getCustomer();
		
		//---------------------------------check--------------------------
		if (purchase.getStatus().equals("paid")) {
            throw new PurchaseAlreadyPaidException();
        }
		
		List<Long> notAvailableList = new ArrayList<Long>();
		double totalPayment = 0;
		for(Purchasebook pb: purchase.getPurchasebooks()) {
			if(pb.getBook().getWarehouse().getAmount()<=0) {
				notAvailableList.add(id);
			}
			totalPayment += pb.getBook().getWarehouse().getPrice();
		}
		
		if(notAvailableList.size()>0) {
			throw new BookNotAvailableException(notAvailableList);
		}
		
		if(customer.getBalance() - totalPayment < 0 ) {
			throw new CustomerNotEnoughMoneyException(customer.getId());
		}
		
		//------------------------act--------------------------------
		purchase.getPurchasebooks().forEach(item ->{
			Warehouse wh = item.getBook().getWarehouse();
			int oldAmount = wh.getAmount();
			wh.setAmount(--oldAmount);
			warehouseRepository.save(wh);
		});
		
		purchase.setStatus("paid");
		Purchase saved = purchaseRepository.save(purchase);

		customer.setBalance(customer.getBalance() - totalPayment);
		customerRepository.save(customer);
		
		Optional<Store> _store = storeRepository.findById(STORE_ID);
		if(_store.isPresent()) {
			Store store = _store.get();
			store.setBalance(store.getBalance() + totalPayment);
			storeRepository.save(store);
		}
	}
	
	public void cancelPurchase(long id) throws StoreNotEnoughMoneyException, PurchaseAlreadyCanceledException, PurchaseNotFoundException  {
		Optional<Purchase> _purchase = purchaseRepository.findById(id);
		if (!_purchase.isPresent()) {
            throw new PurchaseNotFoundException(id);
        }
		Purchase purchase = _purchase.get();
		Customer customer = purchase.getCustomer();
		
		if (purchase.getStatus().equals("cancel")) {
            throw new PurchaseAlreadyCanceledException();
        }
		
		double totalPayment = 0;
		for(Purchasebook pb: purchase.getPurchasebooks()) {
			Warehouse wh = pb.getBook().getWarehouse();
			int oldAmount = wh.getAmount();
			totalPayment += wh.getPrice();
			wh.setAmount(++oldAmount);
			warehouseRepository.save(wh);
		}

		Optional<Store> _store = storeRepository.findById(STORE_ID);
		if(_store.isPresent()) {
			Store store = _store.get();
			if(store.getBalance() - totalPayment <= 0 ) {
	            throw new StoreNotEnoughMoneyException(STORE_ID);
			}
			store.setBalance(store.getBalance() - totalPayment);
			storeRepository.save(store);
		}
		
		customer.setBalance(customer.getBalance() + totalPayment);
		purchase.setStatus("cancel");
		purchaseRepository.save(purchase);
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
			PurchaseView pv = new PurchaseView(p);
			purchaseViews.add(pv);
		}
		view.purchases = purchaseViews;
		return view;
	}

}

