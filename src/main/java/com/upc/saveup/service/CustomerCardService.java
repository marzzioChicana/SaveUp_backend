package com.upc.saveup.service;

import com.upc.saveup.model.CustomerCard;

import java.util.List;

public interface CustomerCardService {
    public abstract void addCardToCustomer(int customerId,int cardId);

    public abstract List<CustomerCard> getAllCustomerCards();

}
