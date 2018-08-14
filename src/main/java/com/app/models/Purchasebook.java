package com.app.models;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "purchasebook")
public class Purchasebook implements Serializable{
	
	@Id
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	
	@Id
    @ManyToOne
    @JoinColumn(name = "purchase_id")
	private Purchase purchase;
	
    @Column(name = "amount")
	private int amount; 
    
    public Purchasebook() {
    	
    }
    
    public Purchasebook(Book book, Purchase purchase, int amount) {
        this.book = book;
        this.purchase = purchase;
        this.amount = amount;
    }
    
    public Purchasebook(Book book, Purchase purchase) {
        this.book = book;
        this.purchase = purchase;
        this.amount = 1;
    }
	
	
	public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
	
    public Purchase getPurchase() {
        return this.purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
    
    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
	
}
