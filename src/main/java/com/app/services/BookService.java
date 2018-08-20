package com.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;
import com.app.utilities.*;
import com.app.views.*;

@Service
public class BookService {
	
	@Autowired
    private BookRepository repository;
	
	public Book createBook(BookView view) throws EntityAlreadyExistsException, InvalidInputDataException {
		if(view.id != null && repository.existsById(view.id)) {
			throw new EntityAlreadyExistsException("book", view.id);
		}
		Book book = new Book(view.id, view.title, view.author);
        return repository.save(book);
    }
	
	public Book getBook(Long id) throws EntityNotFoundException, InvalidInputDataException {
		if(id == null) {
			throw new InvalidInputDataException("id");
		}
		Optional<Book> book = repository.findById(id);
		if (!book.isPresent()) {
            throw new EntityNotFoundException("book",id);
        }
        return book.get();
    }

	public List<Book> getAllBooks() {
		List<Book> books = repository.findAll();
        return books;
    }
	
	public void deleteAllBooks() {
		repository.deleteAll();
	}
}
