package com.app.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.app.models.*;

public interface StoreRepository extends CrudRepository<Store, Long>{
	List<Store> findByTitle(String title);
}
