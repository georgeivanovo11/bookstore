package com.app.utilities;

public class InvalidInputDataException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidInputDataException(String fieldName ) {
		super("The input object is invalid. The field '"+ fieldName+ "' is requied");
	}
}
