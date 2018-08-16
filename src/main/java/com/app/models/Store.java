package com.app.models;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.app.views.*;

@Entity
@Table(name = "store")
public class Store{
	
	private Long id;
	private String title;
	private Double balance;

	public Store()
	{}
	
	public Store(Long id, String title, Double balance) {
		this.id = id;
		this.title = title;
		this.balance = balance;
	}
	
	public Store(StoreView view) {
		this.id = view.id;
		this.title = view.title;
		this.balance = view.balance;
	}
	
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated",
	                  strategy="com.app.utilities.UseIdOrGenerate")
	@Column(name = "store_id",nullable = false)
	public Long getId() {
        return this.id;
    }
	
	public void setId(Long id){
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
	public Double getBalance(){
        return this.balance;
    }
	
	public void setBalance(Double balance){
		this.balance = balance;
	}
}
