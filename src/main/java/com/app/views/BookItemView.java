package com.app.views;

public class BookItemView {
	public long id;
	public double price;
	public int amount;
	
	public BookItemView() {
		
	}
	
	public BookItemView(long id, int amount, double price) {
		this.id = id;
		this.amount = amount;
		this.price = price;
	}
}
