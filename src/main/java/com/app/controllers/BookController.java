package com.app.controllers;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.models.*;
import com.app.services.*;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;

	@PostMapping("/books")
	public @ResponseBody ResponseEntity<HttpStatus> createOne(@RequestBody Book book){
		return service.createBook(book);
	}
	
	@GetMapping("/books")
	public @ResponseBody Book getOne(@RequestParam("id") long id) throws EntityNotFoundException {
		return service.getBook(id);
	}
	
	@GetMapping("/books/all")
	public @ResponseBody Iterable<Book> getAll() {
		return service.getAllBooks();
	}
	
	@DeleteMapping("/books")
	public @ResponseBody ResponseEntity<HttpStatus> deleteOne(@RequestParam("id") long id) throws EntityNotFoundException {
		return service.deleteBook(id);
	}
	
	@PutMapping("/books")
	public @ResponseBody ResponseEntity<HttpStatus> updateOne(@RequestBody Book book) throws EntityNotFoundException {
		return service.updateBook(book);
	}
}