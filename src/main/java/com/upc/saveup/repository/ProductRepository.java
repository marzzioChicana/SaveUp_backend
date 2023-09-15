package com.upc.saveup.repository;

import com.upc.saveup.model.Company;
import com.upc.saveup.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCompanyId(Integer companyId);
    boolean existsByNameAndCompany(String name, Company company);
}
