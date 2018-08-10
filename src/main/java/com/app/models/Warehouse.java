package com.app.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "warehouse")
public class Warehouse {
	private long id;
	private int amount;
	private double price;
	private Book book;
    
    
    public Warehouse() 
    {}
    
    public Warehouse(Book book, int amount, double price) {
		this.book = book;
		this.amount = amount;
		this.price = price;
	}
	
    public Warehouse(int amount, double price) {
		this.amount = amount;
		this.price = price;
	}
    
	@Id
	@Column(name = "warehouse_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
        return this.id;
    }
	public void setId(long id){
		this.id = id;
	}
	
	@OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "book_id")
    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

	@Column(name = "amount")
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
	@Column(name = "price")
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

}