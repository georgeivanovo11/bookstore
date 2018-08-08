package com.app.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.app.models.*;


public interface BookRepository extends CrudRepository<Book, Long>{
	List<Book> findByTitle(String title);
}

