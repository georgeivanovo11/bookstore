package com.app.views;

import com.app.models.*;

public class PurchaseView {
	public long id;
	public String status; 
	public double totalPayment;
	public long customer_id;
	public long[] books;
	
	public PurchaseView(){
	}
	
	public PurchaseView(Purchase purchase){
		this.status = purchase.getSatus();
		this.totalPayment = purchase.getTotalPayment();
		this.customer_id = purchase.getCustomer().getId();
		
	}
}