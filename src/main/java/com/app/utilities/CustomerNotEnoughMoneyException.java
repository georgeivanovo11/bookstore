package com.app.utilities;

public class CustomerNotEnoughMoneyException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public CustomerNotEnoughMoneyException(long id) {
		super("The customer with id: "+id+" doesn't have enough money!");
	}

}
