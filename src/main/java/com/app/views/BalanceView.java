package com.app.views;

import com.app.models.*;
import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceView {
	public Double balance;
	
	public BalanceView(){
		
	}
	
	@JsonCreator
	public BalanceView(@JsonProperty(value = "balance", required = true) Double balance) throws InvalidInputDataException{
		if(balance==null) {
			 throw new InvalidInputDataException("balance");
		 }
		this.balance = balance;
	}
	

}
