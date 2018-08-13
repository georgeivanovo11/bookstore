package com.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class BookService {
	
	@Autowired
    private BookRepository repository;
	
	@Autowired
	private WarehouseRepository wRepository;
	
	public Book createBook(Book book) {
		if(repository.existsById(book.getId())) {
			book.setId(0);
		}
        return repository.save(book);
    }
	
	public Book getBook(Long id) throws EntityNotFoundException {
		Optional<Book> book = repository.findById(id);
		if (!book.isPresent()) {
			String message = "Book with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
        return book.get();
    }
	
	public Iterable<Book> getAllBooks() {
		Iterable<Book> books = repository.findAll();
        return books;
    }
	
	public List<BookViewWithAmount> getAllBooksWithAmount() {
		Iterable<Book> books = repository.findAll();
		List<BookViewWithAmount> views = new ArrayList<BookViewWithAmount>();
		for(Book book: books) {
			BookViewWithAmount view = new BookViewWithAmount();
			view.id = book.getId();
			view.title = book.getTitle();
			view.amount = book.getWarehouse().getAmount();
			views.add(view);
		}
        return views;
    }
	
	public int createDelivery(DeliveryView view) {
		int numberOfSavedBook=0;
		for(BookItemView item: view.books) {
			Optional<Book> _book = repository.findById(item.id);
			if (!_book.isPresent()) {
				continue;
	        }
			Book book = _book.get();
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
			numberOfSavedBook++;
		}
		return numberOfSavedBook;
    }
	
//	public ResponseEntity<HttpStatus> deleteBook(Long id) throws EntityNotFoundException {
//		Optional<Book> book = repository.findById(id);
//		if (!book.isPresent()) {
//			String message = "Book with id: " + id + " doesn't exist!";
//            throw new EntityNotFoundException(message);
//        }
//		repository.delete(book.get());
//        return new ResponseEntity<>(NO_CONTENT);
//	}
//	
//	public ResponseEntity<HttpStatus> updateBook(Book book) {
//		if(!repository.existsById(book.getId())) {
//			String message = "Book with id: " + book.getId() + " doesn't exist!";
//			throw new EntityNotFoundException(message);
//		}
//		repository.save(book);
//        return new ResponseEntity<>(ACCEPTED);
//	}
}
