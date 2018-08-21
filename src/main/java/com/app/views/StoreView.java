package com.app.views;

import com.app.models.*;
import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreView {
	public Long id;
	public String title;
	public Double balance;
	
	public StoreView(){
		this.id = 0L;
		this.title = "";
		this.balance = 0D;
	}
	
	@JsonCreator
	public StoreView(@JsonProperty(value = "id") Long id,
			         @JsonProperty(value = "title", required = true) String title,
			         @JsonProperty(value = "balance", required = true) Double balance){
	    this.id = id;
	    this.title = title;
	    this.balance = balance;
	}
	
	public StoreView(Store store){
	    this.id = store.getId();
	    this.title = store.getTitle();
	    this.balance = store.getBalance();
	}
}
