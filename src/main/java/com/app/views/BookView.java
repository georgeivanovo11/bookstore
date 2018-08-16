package com.app.views;

import com.app.models.*;

public class BookView {

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

