package com.upc.saveup.repository;

import com.upc.saveup.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Customer findByPhoneNumber(String phoneNumber);
    Customer findByEmail(String email);
    Customer findByNameAndLastNameAndPhoneNumber(String name, String lastName, String phoneNumber);

    Customer findByEmailAndPassword(String email, String password);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByEmailAndPhoneNumber(String email, String phoneNumber);
}
