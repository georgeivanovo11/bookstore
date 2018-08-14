package com.app.utilities;

import java.util.List;

public class BookNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BookNotFoundException(long id) {
		super("The book with id: "+id+" doesn't exist!");
	}	
}