package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.models.*;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>{
	Optional<Warehouse> findByBook_id(long id);

}

