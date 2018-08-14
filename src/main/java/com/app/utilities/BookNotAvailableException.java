package com.app.utilities;

import java.util.List;

public class BookNotAvailableException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BookNotAvailableException(long id) {
		super("The book with id: "+id+" is not available!");
	}
	
	public BookNotAvailableException(List<Long> list) {
		super(BookNotAvailableException.generateMessage(list));
	}
	
	public static String generateMessage(List<Long> list) {
		String message = "This books are not available: ";
		for(long item : list) {
			message += item + ", ";
		}
		return message;
	}
}
