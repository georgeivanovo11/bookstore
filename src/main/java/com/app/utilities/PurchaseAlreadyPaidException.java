package com.app.utilities;

public class PurchaseAlreadyPaidException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public PurchaseAlreadyPaidException() {
		super("The purchase has already paid!");
	}	

}
