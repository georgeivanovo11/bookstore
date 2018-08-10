package com.app.views;

import com.app.models.*;

public class StoreView {
	public long id;
	public String title;
	public double balance;
	
	public StoreView(){
		this.id = 0;
		this.title = "";
		this.balance = 0;
	}
	
	public StoreView(Store store){
	    this.id = store.getId();
	    this.title = store.getTitle();
	    this.balance = store.getBalance();
	}
}