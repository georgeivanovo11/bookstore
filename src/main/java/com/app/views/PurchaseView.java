package com.app.views;

import com.app.models.*;

public class PurchaseView {
	public Long id;
	public String status; 
	public Double totalPayment;
	public Long customer_id;
	public long[] books;
	
	public PurchaseView(){
	}
	
	public PurchaseView(Long id, String status, Double tp, Long customer_id){
		this.id = id;
		this.status = status;
		this.totalPayment = tp;
		this.customer_id = customer_id;
		this.books = new long [0];
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