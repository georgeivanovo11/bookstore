package com.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.views.*;
import com.app.repositories.BookRepository;
import com.app.repositories.WarehouseRepository;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;

@Service
public class DeliveryService {
	
	@Autowired
    private BookRepository repository;
	
	@Autowired
	private WarehouseRepository wRepository;
	
	public void deliver(DeliveryView view) throws EntityNotFoundException, InvalidInputDataException {
		for(BookItemView item: view.books) {
			if(item.id == null) {throw new InvalidInputDataException("books.id");}
			if(item.amount == null) {throw new InvalidInputDataException("books.amount");}
			if(item.price == null) {throw new InvalidInputDataException("books.price");}
			Optional<Book> _book = repository.findById(item.id);
			if (!_book.isPresent()) {
				throw new EntityNotFoundException("book", item.id);
	        }
		}
		
		for(BookItemView item: view.books) {
			Book book = repository.findById(item.id).get();
			Warehouse whouse;
			Optional<Warehouse> _whouse = wRepository.findByBook_id(book.getId());
			if (!_whouse.isPresent()) {
				whouse = new Warehouse(book,item.amount, item.price);
	        }
			else {
				whouse = _whouse.get();
				int amount = whouse.getAmount();
				whouse.setAmount(amount+item.amount);
				whouse.setPrice(item.price);
			}
			wRepository.save(whouse);
		}
    }
}