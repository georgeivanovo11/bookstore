package com.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	
	public Purchase getPurchase(long id) throws EntityNotFoundException {
		Optional<Purchase> purchase = purchaseRepository.findById(id);
		if (!purchase.isPresent()) {
            throw new EntityNotFoundException("purchase",id);
        }
        return purchase.get();
    }
	
	public Purchase createPurchase(PurchaseView view) throws InvalidInputDataException, EntityNotFoundException, BookNotAvailableException, CustomerNotEnoughMoneyException {
		//----------------------------check---------------------------------------		
		Optional<Customer> _customer = customerRepository.findById(view.customer_id);
		if (!_customer.isPresent()) {
			throw new EntityNotFoundException("customer",view.customer_id);
        }
		Customer customer = _customer.get();
		
		double totalPayment = 0;
		for(long id: view.books) {
			Optional<Book> _book = bookRepository.findById(id);
			if (!_book.isPresent()) {
				throw new EntityNotFoundException("book",id);
	        }
			Book book = _book.get();
			if(book.getWarehouse()==null || book.getWarehouse().getAmount() <=0 ) {
				throw new BookNotAvailableException(id);
			}
			totalPayment += book.getWarehouse().getPrice();
		}
		
		if(customer.getBalance() - totalPayment < 0 ) {
			throw new CustomerNotEnoughMoneyException(view.customer_id);
		}
	
		//-----------------------------act-------------------------------------------
		
		Purchase purchase = new Purchase(view.id,"pending");
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
//	
//	public void payPurchase(long id) throws PurchaseNotFoundException, PurchaseAlreadyPaidException, BookNotAvailableException, CustomerNotEnoughMoneyException {
//		Optional<Purchase> _purchase = purchaseRepository.findById(id);
//		if (!_purchase.isPresent()) {
//            throw new PurchaseNotFoundException(id);
//        }
//		Purchase purchase = _purchase.get();
//		Customer customer = purchase.getCustomer();
//		
//		//---------------------------------check--------------------------
//		if (purchase.getStatus().equals("paid")) {
//            throw new PurchaseAlreadyPaidException();
//        }
//		
//		List<Long> notAvailableList = new ArrayList<Long>();
//		double totalPayment = 0;
//		for(Purchasebook pb: purchase.getPurchasebooks()) {
//			if(pb.getBook().getWarehouse().getAmount()<=0) {
//				notAvailableList.add(id);
//			}
//			totalPayment += pb.getBook().getWarehouse().getPrice();
//		}
//		
//		if(notAvailableList.size()>0) {
//			throw new BookNotAvailableException(notAvailableList);
//		}
//		
//		if(customer.getBalance() - totalPayment < 0 ) {
//			throw new CustomerNotEnoughMoneyException(customer.getId());
//		}
//		
//		//------------------------act--------------------------------
//		purchase.getPurchasebooks().forEach(item ->{
//			Warehouse wh = item.getBook().getWarehouse();
//			int oldAmount = wh.getAmount();
//			wh.setAmount(--oldAmount);
//			warehouseRepository.save(wh);
//		});
//		
//		purchase.setStatus("paid");
//		Purchase saved = purchaseRepository.save(purchase);
//
//		customer.setBalance(customer.getBalance() - totalPayment);
//		customerRepository.save(customer);
//		
//		Optional<Store> _store = storeRepository.findById(STORE_ID);
//		if(_store.isPresent()) {
//			Store store = _store.get();
//			store.setBalance(store.getBalance() + totalPayment);
//			storeRepository.save(store);
//		}
//	}
//	
//	public void cancelPurchase(long id) throws StoreNotEnoughMoneyException, PurchaseAlreadyCanceledException, PurchaseNotFoundException  {
//		Optional<Purchase> _purchase = purchaseRepository.findById(id);
//		if (!_purchase.isPresent()) {
//            throw new PurchaseNotFoundException(id);
//        }
//		Purchase purchase = _purchase.get();
//		Customer customer = purchase.getCustomer();
//		
//		if (purchase.getStatus().equals("cancel")) {
//            throw new PurchaseAlreadyCanceledException();
//        }
//		
//		double totalPayment = 0;
//		for(Purchasebook pb: purchase.getPurchasebooks()) {
//			Warehouse wh = pb.getBook().getWarehouse();
//			int oldAmount = wh.getAmount();
//			totalPayment += wh.getPrice();
//			wh.setAmount(++oldAmount);
//			warehouseRepository.save(wh);
//		}
//
//		Optional<Store> _store = storeRepository.findById(STORE_ID);
//		if(_store.isPresent()) {
//			Store store = _store.get();
//			if(store.getBalance() - totalPayment <= 0 ) {
//	            throw new StoreNotEnoughMoneyException(STORE_ID);
//			}
//			store.setBalance(store.getBalance() - totalPayment);
//			storeRepository.save(store);
//		}
//		
//		customer.setBalance(customer.getBalance() + totalPayment);
//		purchase.setStatus("cancel");
//		purchaseRepository.save(purchase);
//	}
	
	public void deleteAllPurchases(){
		this.purchaseRepository.deleteAll();
	}

}

