package com.app.models;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;

import com.app.utilities.*;
import com.app.views.*;
import com.app.models.*;


@Entity
@Table(name = "book")
public class Book {
	
	private long id;
    private String title;
	private String author;
    private Warehouse warehouse;
    private Set<Purchasebook> purchasebooks;

	public Book() 
	{}
	
	public Book(String title, String author, Warehouse warehouse) {
		this.title = title;
		this.author = author;
		this.warehouse = warehouse;
        purchasebooks = new HashSet<>();
	}
	
	public Book(long id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = author;
	}
	
	public Book(BookView view) {
		this.id = view.id;
		this.title = view.title;
		this.author = view.author;
	}
	
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated",
	                  strategy="com.app.utilities.UseIdOrGenerate"
	)
	@Column(name = "book_id",nullable = false)
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
 
	@Column(name = "author")
	public String getAuthor(){
        return this.author;
    }
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	@OneToOne(mappedBy = "book")
    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
    	this.warehouse = warehouse;
    }
    
    @OneToMany(mappedBy = "book",  cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Purchasebook> getPurchasebooks() {
        return this.purchasebooks;
    }

    public void setPurchasebooks(Set<Purchasebook> purchasebooks) {
        this.purchasebooks = purchasebooks;
    }
   
}


