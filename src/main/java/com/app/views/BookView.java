package com.app.views;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.app.models.*;

public class BookView{

	public Long id;
	
	public String title;
	
	public String author;
	
	public BookView(){
	}
	
	public BookView(Book book){
	    this.id = book.getId();
	    this.title = book.getTitle();
	    this.author = book.getAuthor();
	}
	
	public BookView(Long id, String title, String author){
	    this.id = id;
	    this.title = title;
	    this.author = author;
	}
		
}

