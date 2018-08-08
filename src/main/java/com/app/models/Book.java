package com.app.models;
import java.io.Serializable;
import javax.persistence.*;
import com.app.views.*;

@Entity
@Table(name = "book")
public class Book implements Serializable {
	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	@Column(name = "title")
	public String title;
 
	@Column(name = "author")
	public String author;
	
	public Book(String title, String author) {
		this.title = title;
		this.author = author;
	}
	
	public Book() {
	}
	
	@Override
	public String toString() {
		return String.format("Book[id=%d, title='%s', author='%s']", id, title, author);
	}
	
}
