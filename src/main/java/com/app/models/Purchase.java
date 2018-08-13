package com.app.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.app.views.*;
import com.app.models.*;

@Entity
@Table(name = "purchase")
public class Purchase  {
	
	@Id
	@Column(name = "purchase_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "totalpayment")
	private double totalPayment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
    private Customer customer;
	
    @OneToMany(mappedBy = "purchase")
    private Set<Purchasebook> purchasebooks;


	public Purchase()
	{}
	
	public Purchase(String status) {
		this.status = status;
        purchasebooks = new HashSet<>();
	}
	
	
	public long getId() {
        return this.id;
    }
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getStatus() {
        return this.status;
    }
	
	public void setStatus(String status){
		this.status = status;
	}
	
	
	public double getTotalPayment(){
        return this.totalPayment;
    }
	
	public void setTotalPayment(double totalPayment){
		this.totalPayment = totalPayment;
	}
	
    public Customer getCustomer() {
        return this.customer;
    }
	
	public void setCustomer(Customer customer) {
        this.customer = customer;
    }
	
	public Set<Purchasebook> getPurchasebooks() {
        return this.purchasebooks;
    }

    public void setPurchasebooks(Set<Purchasebook> purchasebooks) {
        this.purchasebooks = purchasebooks;
    }
	
}
