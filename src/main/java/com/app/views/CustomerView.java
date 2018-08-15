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
	
	public CustomerView(long id, String name, double balance){
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