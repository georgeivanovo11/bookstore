package com.app.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
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

import org.hibernate.annotations.GenericGenerator;

import com.app.views.*;
import com.app.models.*;

@Entity
@Table(name = "purchase")
public class Purchase  {
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated",
	                  strategy="com.app.utilities.UseIdOrGenerate")
	@Column(name = "purchase_id",nullable = false)
	private Long id;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "totalpayment")
	private Double totalPayment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
    private Customer customer;
	
    @OneToMany(mappedBy = "purchase")
    private Set<Purchasebook> purchasebooks;


	public Purchase()
	{}
	
	public Purchase(Long id, String status) {
		this.id = id;
		this.status = status;
        purchasebooks = new HashSet<>();
	}
	
	public Purchase(String status, Double totalPayment, Customer customer) {
		this.status = status;
		this.totalPayment = totalPayment;
		this.customer = customer;
        purchasebooks = new HashSet<>();
	}
	
	
	public Long getId() {
        return this.id;
    }
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getStatus() {
        return this.status;
    }
	
	public void setStatus(String status){
		this.status = status;
	}
	
	
	public Double getTotalPayment(){
        return this.totalPayment;
    }
	
	public void setTotalPayment(Double totalPayment){
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
