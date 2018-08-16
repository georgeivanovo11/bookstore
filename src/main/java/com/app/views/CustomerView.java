package com.app.views;

import com.app.models.*;

public class CustomerView {
	public Long id;
	public String name;
	public Double balance;
	
	public CustomerView(){
	}
	
	public CustomerView(Long id, String name, Double balance){
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