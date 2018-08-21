package com.app.views;

import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryView {
	public BookItemView[] books;
	
	public DeliveryView() {
		
	}
	
	@JsonCreator
	public DeliveryView(@JsonProperty(value = "books", required = true) BookItemView[] books){
		this.books = books;
	}
}
