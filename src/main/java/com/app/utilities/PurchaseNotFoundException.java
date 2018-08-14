package com.app.utilities;

public class PurchaseNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public PurchaseNotFoundException(long id) {
		super("The purchase with id: "+id+" doesn't exist!");
	}	
	
}
