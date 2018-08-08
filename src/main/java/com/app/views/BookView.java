package com.app.views;

import java.io.Serializable;

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
		this.id = book.id;
		this.title = book.title;
		this.author = book.author;
	}
}
