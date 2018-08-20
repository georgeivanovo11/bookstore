package com.app.views;

import com.app.models.*;
import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseView {
	public Long id;
	public String status; 
	public Double totalPayment;
	public Long customer_id;
	public long[] books;
	
	public PurchaseView(){
	}
	
	@JsonCreator
	public PurchaseView(@JsonProperty(value = "id") Long id,
						@JsonProperty(value = "customer_id", required = true) Long customer_id,
						@JsonProperty(value = "books", required = true) long[] books) throws InvalidInputDataException
	{
		this.id=id;
		if(customer_id==null) {
			 throw new InvalidInputDataException("customer");
		 }
		this.customer_id = customer_id;
		if(books==null) {
			 throw new InvalidInputDataException("books");
		 }
		this.books = books;
	}
	
	public PurchaseView( Long id,String status, Double tp, Long customer_id){
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
