package com.app.models;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "store")
public class Store implements Serializable {
	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@Column(name = "store_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	@Column(name = "title")
	public String title;
 
	@Column(name = "balance")
	public double balance;
	
	public Store(String title, double balance) {
		this.title = title;
		this.balance = balance;
	}
	
	public Store() {
	}
}
