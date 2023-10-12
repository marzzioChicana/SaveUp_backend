package com.upc.saveup.controller;

import com.upc.saveup.exception.ResourceNotFoundException;
import com.upc.saveup.exception.ValidationException;
import com.upc.saveup.model.Customer;
import com.upc.saveup.repository.CompanyRepository;
import com.upc.saveup.repository.CustomerRepository;
import com.upc.saveup.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/saveup/v1")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public CustomerController(CustomerRepository customerRepository, CompanyRepository companyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<List<Customer>>(customerRepository.findAll(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/purchase/{customerId}/data
    @GetMapping("/purchase/{customerId}/data")
    public List<Map<String, Object>> getPurchaseData(@PathVariable int customerId) {
        return customerService.getPurchaseData(customerId);
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers/login
    //Method: POST
    @Transactional(readOnly = true)
    @PostMapping("/customers/login")
    public ResponseEntity<Customer> getCustomerByEmailAndPassword(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Customer customer = customerRepository.findByEmailAndPassword(email, password);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return null;
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers/recover
    //Method: POST
    @Transactional(readOnly = true)
    @PostMapping("/customers/recover")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");

        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return null;
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers
    //Method: POST
    @Transactional
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        validateCustomer(customer);
        existsCompanyByEmail(customer);
        existsCustomerByEmailAndPhoneNumber(customer);
        existsCustomerByEmail(customer);
        existsCustomerByPhoneNumber(customer);
        customer.setPoints(0);
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers/{id}
    //Method: PUT
    @Transactional
    @PutMapping("/customers/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") int id,@RequestBody Customer customer){
        boolean isExist=customerService.isCustomerExist(id);
        if(isExist){
            validateCustomer(customer);
            customer.setId(id);
            customerService.updateCustomer(customer);
            return new ResponseEntity<>("Customer is updated succesfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al actualizar el customer");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers/{id}/points
    //Method: PUT
    @Transactional
    @PutMapping("/customers/{id}/points")
    public ResponseEntity<Object> updateCustomerPoints(@PathVariable("id") int id, @RequestBody int newPoints) {
        boolean isExist=customerService.isCustomerExist(id);
        if(isExist){
            Customer customerTemp = customerRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("No se encontro el customer con id: " + id));

            customerTemp.setPoints(newPoints);
            customerService.updatePointsCustomer(customerTemp);
            return new ResponseEntity<>("Customer points is updated succesfully", HttpStatus.OK);
        } else{
            throw new ValidationException("Error al actualizar los points del customer");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/customers/{id}
    //Method: DELETE
    @Transactional
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") int id){
        boolean isExist=customerService.isCustomerExist(id);
        if(isExist){
            customerService.deleteCustomer(id);
            return new ResponseEntity<>("Customer is deleted successfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al eliminar el customer");
        }
    }

    private void validateCustomer(Customer customer){
        String emailRegex = "^[A-Za-z0-9+_.-]+@(hotmail|gmail)\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(customer.getEmail()).matches()) {
            throw new ValidationException("El email debe ser de tipo hotmail.com o gmail.com");
        }else if(customer.getEmail() == null || customer.getEmail().trim().isEmpty()){
            throw new ValidationException("El email debe ser obligatorio");
        }
        if(customer.getName() == null || customer.getName().trim().isEmpty()){
            throw new ValidationException("El nombre debe ser obligatorio");
        }
        if(customer.getLastName() == null || customer.getLastName().trim().isEmpty()){
            throw new ValidationException("El apellido debe ser obligatorio");
        }
        if (customer.getDepartment()==null||customer.getDepartment().trim().isEmpty()){
            throw new ValidationException("El departamento debe ser obligatorio");
        }
        if (customer.getDistrict()==null||customer.getDistrict().trim().isEmpty()){
            throw new ValidationException("El distrito debe ser obligatorio");
        }

        String phoneNumberRegex = "^9\\d{8}$";
        pattern = Pattern.compile(phoneNumberRegex);
        if (!pattern.matcher(customer.getPhoneNumber()).matches()) {
            throw new ValidationException("El numero telefonico debe empezar con 9 y tener 9 digitos");
        } else if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
            throw new ValidationException("El numero telefonico debe ser obligatorio");
        }

        if(customer.getPassword() == null || customer.getPassword().trim().isEmpty()){
            throw new ValidationException("La contraseña debe ser obligatorio");
        }
        if(customer.getRepeatPassword() == null || customer.getRepeatPassword().trim().isEmpty()){
            throw new ValidationException("La confirmación de contraseña debe ser obligatorio");
        }
        if (customer.getPassword().length() < 8 || customer.getPassword().length() > 12) {
            throw new ValidationException("La contraseña debe tener entre 8 y 12 caracteres");
        }

        if (!customer.getPassword().equals(customer.getRepeatPassword())){
            throw new ValidationException("La contraseña y la confirmación de contraseña no coinciden");
        }
    }

    private void existsCustomerByEmailAndPhoneNumber(Customer customer){
        if(customerRepository.existsByEmailAndPhoneNumber(customer.getEmail(), customer.getPhoneNumber())){
            throw new ValidationException("No se puede registrar el usuario porque existe uno con el email y numero telefonico");
        }
    }
    private void existsCustomerByEmail(Customer customer){
        if(customerRepository.existsByEmail(customer.getEmail())){
            throw new ValidationException("No se puede registrar el usuario porque existe una cuenta con el email");
        }
    }
    private void existsCustomerByPhoneNumber(Customer customer){
        if(customerRepository.existsByPhoneNumber(customer.getPhoneNumber())){
            throw new ValidationException("No se puede registrar el usuario porque existe uno con el numero telefonico");
        }
    }

    private void existsCompanyByEmail(Customer customer){
        if(companyRepository.existsByEmail(customer.getEmail())){
            throw new ValidationException("No se puede registrar el usuario porque existe una cuenta con el email");
        }
    }
}
