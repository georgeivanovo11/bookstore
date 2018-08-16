package com.app.views;

public class BookItemView {
	public Long id;
	public Double price;
	public Integer amount;
	
	public BookItemView() {
		
	}
	
	public BookItemView(Long id, Integer amount, Double price) {
		this.id = id;
		this.amount = amount;
		this.price = price;
	}
}
