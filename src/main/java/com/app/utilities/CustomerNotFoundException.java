package com.app.utilities;

public class CustomerNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(long id) {
		super("The customer with id: "+id+" doesn't exist!");
	}
}