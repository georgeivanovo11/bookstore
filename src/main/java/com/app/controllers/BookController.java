package com.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;

	@PostMapping("/books")
	public @ResponseBody ResponseEntity<BookView> createOne(@RequestBody BookView view) throws EntityAlreadyExistsException{
		Book savedBook = service.createBook(view);
		BookView savedBookView = new BookView(savedBook);
		return new ResponseEntity<BookView>(savedBookView, HttpStatus.CREATED);
	}
	
	@GetMapping("/books/{id}")
	public @ResponseBody ResponseEntity<BookView> getOne(@PathVariable("id") long id) throws EntityNotFoundException {
		return new ResponseEntity<BookView>(new BookView(service.getBook(id)), HttpStatus.OK);
	}
	
	@GetMapping("/books")
	public @ResponseBody List<BookView> getAll() {
		Iterable<Book> books = service.getAllBooks();
		List<BookView> bookViews = new ArrayList<BookView>();
		for (Book book : books) {
		    bookViews.add(new BookView(book));
		}
		return bookViews;
	}
	
	
	@PostMapping("/delivery")
	public @ResponseBody ResponseEntity<DeliveryOutView> delivery(@RequestBody String str) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		DeliveryView view = objectMapper.readValue(str, DeliveryView.class);
		int count = service.createDelivery(view);
		DeliveryOutView dov = new DeliveryOutView(count);
		if(count>0) {
			return new ResponseEntity<DeliveryOutView>(dov, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<DeliveryOutView>(dov, HttpStatus.CONFLICT);
		}
	}
	
//	@GetMapping("/books/amount")
//	public @ResponseBody List<BookViewWithAmount> getAllWithAmount() {
//		return service.getAllBooksWithAmount();
//	}
	
//	@PostMapping("/delivery")
//	public @ResponseBody ResponseEntity<HttpStatus> delivery(@RequestBody String str) throws JsonParseException, JsonMappingException, IOException{
//		
//	}
	
//	@DeleteMapping("/books")
//	public @ResponseBody ResponseEntity<HttpStatus> deleteOne(@RequestParam("id") long id) throws EntityNotFoundException {
//		return service.deleteBook(id);
//	}
//	
//	@PutMapping("/books")
//	public @ResponseBody ResponseEntity<HttpStatus> updateOne(@RequestBody BookView view) throws EntityNotFoundException {
//		Book book = new Book(view);
//		return service.updateBook(book);
//	}
}