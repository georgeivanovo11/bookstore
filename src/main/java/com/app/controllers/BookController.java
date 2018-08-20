package com.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.BookNotAvailableException;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@PostMapping("/books")
	public @ResponseBody ResponseEntity<BookView> createOne(@RequestBody BookView view) throws EntityAlreadyExistsException, InvalidInputDataException {
		Book savedBook = service.createBook(view);
		BookView savedBookView = new BookView(savedBook);
		return new ResponseEntity<BookView>(savedBookView, HttpStatus.CREATED);
	}
	
	@GetMapping("/books/{id}")
	public @ResponseBody ResponseEntity<BookView> getOne(@PathVariable("id") Long id) throws EntityNotFoundException, InvalidInputDataException {
		return new ResponseEntity<BookView>(new BookView(service.getBook(id)), HttpStatus.OK);
	}
	
	@GetMapping("/books")
	public @ResponseBody ResponseEntity<List<BookView>> getAll() {
		List<Book> books = service.getAllBooks();
		List<BookView> bookViews = new ArrayList<BookView>();
		for (Book book : books) {
		    bookViews.add(new BookView(book));
		}
		return new ResponseEntity<List<BookView>>(bookViews, HttpStatus.OK);
	}
}