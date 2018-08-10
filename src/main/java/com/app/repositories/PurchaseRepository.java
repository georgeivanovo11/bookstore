package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.models.*;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>
{}
