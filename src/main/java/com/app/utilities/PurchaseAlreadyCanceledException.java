package com.app.utilities;

public class PurchaseAlreadyCanceledException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public PurchaseAlreadyCanceledException() {
		super("The purchase has already canceled!");
	}	

}
