package com.app.views;

import com.app.models.*;
import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerView {
	public Long id;
	public String name;
	public Double balance;
	
	public CustomerView(){
	}
	
	@JsonCreator
	public CustomerView(@JsonProperty(value = "id") Long id,
			 			@JsonProperty(value = "name", required = true) String name,
			 			@JsonProperty(value = "balance", required = true) Double balance) throws InvalidInputDataException
	{
		this.id = id;
		if(name==null) {
			 throw new InvalidInputDataException("name");
		 }
		this.name = name;
		if(balance==null) {
			 throw new InvalidInputDataException("balance");
		 }
		this.balance = balance;
	}
	
	public CustomerView( Long id, String name, Double balance,String mode) 
	{
		this.id = id;
		this.name = name;
		this.balance = balance;
	}
	
	public CustomerView(Customer customer){
	    this.id = customer.getId();
	    this.name = customer.getName();
	    this.balance = customer.getBalance();
	}
}