package com.app.views;

import com.app.models.*;

public class CustomerView {
	public long id;
	public String name;
	public double balance;
	
	public CustomerView(){
		this.id = 0;
		this.name = "";
		this.balance = 0;
	}
	
	public CustomerView(Customer customer){
	    this.id = customer.getId();
	    this.name = customer.getName();
	    this.balance = customer.getBalance();
	}
}