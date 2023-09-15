package com.upc.saveup.service;

import com.upc.saveup.model.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    public abstract Customer createCustomer(Customer employee);
    public abstract void updateCustomer(Customer employee);
    public abstract void updatePointsCustomer(Customer customer);
    public abstract void deleteCustomer(int id);
    public abstract Customer getCustomer(int id);
    public abstract List<Customer> getAllCustomers();
    public abstract boolean isCustomerExist(int id);
    public abstract List<Map<String, Object>> getPurchaseData(int customerId);
}
