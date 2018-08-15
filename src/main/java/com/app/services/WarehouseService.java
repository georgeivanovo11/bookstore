package com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.repositories.WarehouseRepository;

@Service
public class WarehouseService {
	
	@Autowired
    private WarehouseRepository repository;
	
	public void deleteAllLines() {
		repository.deleteAll();
    }

}

