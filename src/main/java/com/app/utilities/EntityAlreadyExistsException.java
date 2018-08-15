package com.app.utilities;

public class EntityAlreadyExistsException  extends Exception {
	private static final long serialVersionUID = 1L;

	public EntityAlreadyExistsException(String entityName, long id) {
		super("The "+entityName+" with id: "+id+" already exists!");
	}	
}
