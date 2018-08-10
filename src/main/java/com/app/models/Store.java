package com.app.models;

import javax.persistence.*;
import com.app.views.*;

@Entity
@Table(name = "store")
public class Store{
	
	private long id;
	private String title;
	private double balance;

	public Store()
	{}
	
	public Store(String title, double balance) {
		this.title = title;
		this.balance = balance;
	}
	
	public Store(StoreView view) {
		this.title = view.title;
		this.balance = view.balance;
	}
	
	@Id
	@Column(name = "store_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
        return this.id;
    }
	
	public void setId(long id){
		this.id = id;
	}
	
	@Column(name = "title")
	public String getTitle() {
        return this.title;
    }
	
	public void setTitle(String title){
		this.title = title;
	}
	
	
	@Column(name = "balance")
	public double getBalance(){
        return this.balance;
    }
	
	public void setBalance(double balance){
		this.balance = balance;
	}
}
