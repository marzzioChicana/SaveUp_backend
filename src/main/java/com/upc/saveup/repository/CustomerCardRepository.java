package com.upc.saveup.repository;

import com.upc.saveup.model.CustomerCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCardRepository extends JpaRepository<CustomerCard,Integer> {
}
