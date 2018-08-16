package com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.models.Book;
import com.app.models.Warehouse;
import com.app.repositories.WarehouseRepository;

@Service
public class WarehouseService {
	
	@Autowired
    private WarehouseRepository repository;
	
	public void deleteAllLines() {
		repository.deleteAll();
    }
	
	public void createLine(Book book, Integer amount, Double price) {
		repository.save(new Warehouse(book,amount,price));
	}

}

