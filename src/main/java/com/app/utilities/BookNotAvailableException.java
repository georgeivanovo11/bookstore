package com.app.utilities;

import java.util.List;

public class BookNotAvailableException  extends Exception {
	private static final long serialVersionUID = 1L;
	public BookNotAvailableException(Long id) {
		super("The book with id: "+id+" is not available now!");
	}	
	
	public BookNotAvailableException(List<Long> ids) {
		super(generateMessage(ids));
	}
	
	public static String generateMessage(List<Long> ids){
		String message = "The books with ids: ";
		for(Long id: ids) {
			message +=id.toString() + ", ";
		}
		message+= "are not available now!";
		return message;
	}
}
