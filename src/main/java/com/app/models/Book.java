package com.app.models;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book implements Serializable {
	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "title")
	private String title;
 
	@Column(name = "author")
	private String author;
	
	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}
	
	
}
