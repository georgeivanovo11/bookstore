package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.app.models.*;
import com.app.repositories.*;

@RestController
public class BookController {
	
	@Autowired
	BookRepository repository;

	@RequestMapping("/save")
	public String process(){
		return "Done1";
	}
}
