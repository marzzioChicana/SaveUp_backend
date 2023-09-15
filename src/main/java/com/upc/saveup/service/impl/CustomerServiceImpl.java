package com.upc.saveup.service.impl;

import com.upc.saveup.model.Customer;
import com.upc.saveup.repository.CustomerRepository;
import com.upc.saveup.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    @Override
    public void updateCustomer(Customer customer){
        customerRepository.save(customer);
    }

    @Override
    public void updatePointsCustomer(Customer customer){ customerRepository.save(customer); }
    @Override
    public Customer getCustomer(int id){
        return customerRepository.findById(id).get();
    }
    @Override
    public void deleteCustomer(int id){
        customerRepository.deleteById(id);
    }
    @Override
    public List<Customer> getAllCustomers(){
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public boolean isCustomerExist(int id){
        return customerRepository.existsById(id);
    }

    @Override
    public List<Map<String, Object>> getPurchaseData(int customerId) {
        String query = "SELECT cu.id as customer_id, pr.name, co.name as empresa, o.date, pa.pay_address " +
                "FROM saveup.customer cu " +
                "INNER JOIN saveup.pay pa ON cu.id = pa.customer_id " +
                "INNER JOIN saveup.orders o ON pa.id = o.pay_id " +
                "INNER JOIN saveup.cart ca ON o.id = ca.order_id " +
                "INNER JOIN saveup.product pr ON ca.product_id = pr.id " +
                "INNER JOIN saveup.company co ON pr.company_id = co.id " +
                "WHERE cu.id = ?";
        return jdbcTemplate.queryForList(query, customerId);
    }
}
