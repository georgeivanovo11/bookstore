package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.models.*;

public interface StoreRepository extends JpaRepository<Store, Long>{
	Optional<Store> findByTitle(String title);
}
