package com.app.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.ACCEPTED;



@Service
public class BookService {
	
	@Autowired
    private BookRepository repository;
	
	public ResponseEntity createBook(Book book) {
		if(repository.existsById(book.id)) {
			book.id=0;
		}
        repository.save(book);
        return new ResponseEntity<>(CREATED);

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
	
	public ResponseEntity deleteBook(Long id) throws EntityNotFoundException {
		Optional<Book> book = repository.findById(id);
		if (!book.isPresent()) {
			String message = "Book with id: " + id + " doesn't exist!";
            throw new EntityNotFoundException(message);
        }
		repository.delete(book.get());
        return new ResponseEntity<>(NO_CONTENT);
	}
	
	public ResponseEntity updateBook(Book book) {
		if(!repository.existsById(book.id)) {
			String message = "Book with id: " + book.id + " doesn't exist!";
			throw new EntityNotFoundException(message);
		}
		repository.save(book);
        return new ResponseEntity<>(ACCEPTED);
	}
}
