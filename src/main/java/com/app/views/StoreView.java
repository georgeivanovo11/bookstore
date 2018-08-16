package com.app.views;

import com.app.models.*;

public class StoreView {
	public Long id;
	public String title;
	public Double balance;
	
	public StoreView(){
		this.id = 0L;
		this.title = "";
		this.balance = 0D;
	}
	
	public StoreView(Long id, String title, Double balance){
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