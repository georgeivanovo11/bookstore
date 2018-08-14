package com.app.utilities;

public class StoreNotEnoughMoneyException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public StoreNotEnoughMoneyException(long id) {
		super("The store with id: "+id+" doesn't have enough money!");
	}

}
