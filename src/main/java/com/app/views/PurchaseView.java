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
		this.id = purchase.getId();
		this.status = purchase.getStatus();
		this.totalPayment = purchase.getTotalPayment();
		this.customer_id = purchase.getCustomer().getId();
		this.books = new long [purchase.getPurchasebooks().size()];
		int index = 0;
		for(Purchasebook pb: purchase.getPurchasebooks()) {
			this.books[index]=pb.getBook().getId();
			index++;
		}
	}
}