package com.upc.saveup.controller;

import com.upc.saveup.model.CustomerCard;
import com.upc.saveup.repository.CustomerCardRepository;
import com.upc.saveup.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/saveup/v1")
public class CustomerCardController {
    @Autowired
    private CustomerService customerService;
    private final CustomerCardRepository customerCardRepository;

    public CustomerCardController(CustomerCardRepository customerCardRepository){
        this.customerCardRepository = customerCardRepository;
    }
    //EndPoint: localhost:8080/api/saveup/v1/customers/cards
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/customers/cards")
    public ResponseEntity<List<CustomerCard>> getAllCustomerCards(){
        return new ResponseEntity<List<CustomerCard>>(customerCardRepository.findAll(), HttpStatus.OK);
    }
}
