package com.app.utilities;

public class InvalidPurchaseStatusException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidPurchaseStatusException(String status) {
		super("The action can not be executed. Current status '"+status+"' is inappropriate.");
	}	
	
}
