package com.app.views;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.app.models.*;
import com.app.utilities.InvalidInputDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookView{
	
	public Long id;
	public String title;
	public String author;
	
	 @JsonCreator
	 public BookView(@JsonProperty(value = "id") Long id,
			         @JsonProperty(value = "title", required = true, defaultValue= "") String title, 
	                 @JsonProperty(value = "author", required = true, defaultValue= "") String author) throws InvalidInputDataException
	 {
		 this.id = id;
		 if(title==null) {
			 throw new InvalidInputDataException("title");
		 }
		 this.title = title;
		 if(author==null) {
			 throw new InvalidInputDataException("author");
		 }
		 this.author = author;
	 }
	
	public BookView(){
	}
	
	public BookView(Book book){
	    this.id = book.getId();
	    this.title = book.getTitle();
	    this.author = book.getAuthor();
	}
	
	 public BookView(Long id, String title, String author, String mode)
	 {
		 this.id = id;
		 this.title = title;
		 this.author = author;
	 }
}

