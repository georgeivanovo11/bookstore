package com.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.app.views.*;

@Entity
@Table(name = "customer")
public class Customer  {
		
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated",
	                  strategy="com.app.utilities.UseIdOrGenerate")
	@Column(name = "customer_id",nullable = false)
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "balance")
	private double balance;
	
	@OneToMany(
	        mappedBy = "customer", 
	        cascade = CascadeType.ALL, 
	        orphanRemoval = true
	    )
	private List<Purchase> purchases;

	public Customer()
	{}
	
	public Customer(long id, String name, double balance) {
		this.id = id;
		this.name = name;
		this.balance = balance;
		this.purchases = new ArrayList<>();
	}
	
	public Customer(CustomerView view) {
		this.id = view.id;
		this.name = view.name;
		this.balance = view.balance;
		this.purchases = new ArrayList<>();
	}
	
	
	public long getId() {
        return this.id;
    }
	
	public void setId(long id){
		this.id = id;
	}
	
	
	public List<Purchase> getPurchases() {
        return this.purchases;
    }
	
	public void setPurchases(List<Purchase> list){
		this.purchases = list;
	}
	
	public String getName() {
        return this.name;
    }
	
	public void setName(String name){
		this.name = name;
	}
	
	
	public double getBalance(){
        return this.balance;
    }
	
	public void setBalance(double balance){
		this.balance = balance;
	}
	
	public void addPurchase( Purchase purchase) {
		this.purchases.add(purchase);
        purchase.setCustomer(this);
    }
 
    public void removePurchase(Purchase purchase) {
    	this.purchases.remove(purchase);
        purchase.setCustomer(null);
    }
}