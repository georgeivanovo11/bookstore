package com.app.views;


import com.app.models.*;
import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookView{
	
	public Long id;
	public String title;
	public String author;
	
	 @JsonCreator
	 public BookView(@JsonProperty(value="id") Long id,
			         @JsonProperty(value="title", required = true) String title, 
	                 @JsonProperty(value="author", required = true) String author)
	 {
		 this.id = id;
		 this.title = title;
		 this.author = author;
	 }
	
	public BookView(){
	}
	
	public BookView(Book book){
	    this.id = book.getId();
	    this.title = book.getTitle();
	    this.author = book.getAuthor();
	}	
}

