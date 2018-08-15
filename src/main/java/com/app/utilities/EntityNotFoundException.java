package com.app.utilities;

public class EntityNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	public EntityNotFoundException(String entityName, long id) {
		super("The "+entityName+" with id: "+id+" doesn't exist!");
	}	
}
