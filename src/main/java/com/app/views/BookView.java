package com.app.views;

import com.app.models.*;

public class BookView {

	public long id;
	public String title;
	public String author;
	
	public BookView(){
		this.id = 0;
		this.title = "";
		this.author = "";
	}
	
	public BookView(Book book){
	    this.id = book.getId();
	    this.title = book.getTitle();
	    this.author = book.getAuthor();
	}
}
