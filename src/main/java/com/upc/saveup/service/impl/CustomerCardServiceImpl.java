package com.upc.saveup.service.impl;

import com.upc.saveup.model.Card;
import com.upc.saveup.model.Customer;
import com.upc.saveup.model.CustomerCard;
import com.upc.saveup.model.CustomerCardId;
import com.upc.saveup.repository.CardRepository;
import com.upc.saveup.repository.CustomerCardRepository;
import com.upc.saveup.repository.CustomerRepository;
import com.upc.saveup.service.CustomerCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerCardServiceImpl implements CustomerCardService {
    @Autowired
    private CustomerCardRepository customerCardRepository;

    private final CustomerRepository customerRepository;
    private final CardRepository cardRepository;

    public CustomerCardServiceImpl(CustomerRepository customerRepository, CardRepository cardRepository) {
        this.customerRepository = customerRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public void addCardToCustomer(int customerId, int cardId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));

        CustomerCardId customerCardId = new CustomerCardId();
        customerCardId.setCustomerId(customerId);
        customerCardId.setCardId(cardId);

        CustomerCard customerCard = new CustomerCard();
        customerCard.setId(customerCardId);
        customerCard.setCustomer(customer);
        customerCard.setCard(card);

        customerCardRepository.save(customerCard);
    }

    @Override
    public List<CustomerCard> getAllCustomerCards() {
        return (List<CustomerCard>) customerCardRepository.findAll();
    }
}
