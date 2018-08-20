package com.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.models.*;
import com.app.repositories.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.EntityNotFoundException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;

@Service
public class StoreService {
	
	@Autowired
    private StoreRepository sRepository;
	
	@Autowired
    private BookRepository bookRepository;

	
	public Store createStore(StoreView view) throws InvalidInputDataException, EntityAlreadyExistsException {
		if(view.title == null) {
			throw new InvalidInputDataException("title");
		}
		if(view.balance == null) {
			throw new InvalidInputDataException("balance");
		}
		if(view.id != null && sRepository.existsById(view.id)) {
			throw new EntityAlreadyExistsException("store", view.id);
		}
		Store store = sRepository.save(new Store(view.id, view.title, view.balance));
        return sRepository.save(store);
    }
	
	public Store getStore(Long id) throws EntityNotFoundException, InvalidInputDataException {
		Optional<Store> store = sRepository.findById(id);
		if (!store.isPresent()) {
            throw new EntityNotFoundException("store",id);

        }
        return store.get();
    }
	
	public List<Store> getAllStores() {
		List<Store> stores = sRepository.findAll();
        return stores;
    }
	
	public void deleteAllStores() {
		sRepository.deleteAll();
	}
}

