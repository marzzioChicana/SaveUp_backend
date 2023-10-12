package com.upc.saveup.controller;

import com.upc.saveup.exception.ValidationException;
import com.upc.saveup.model.Company;
import com.upc.saveup.repository.CompanyRepository;
import com.upc.saveup.repository.CustomerRepository;
import com.upc.saveup.service.CompanyService;
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
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;

    public CompanyController(CompanyRepository companyRepository, CustomerRepository customerRepository) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
    }

    //EndPoint: localhost:8080/api/saveup/v1/companies
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(){
        return new ResponseEntity<List<Company>>(companyRepository.findAll(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/sale/{companyId}/data
    @Transactional(readOnly = true)
    @GetMapping("/sale/{companyId}/data")
    public List<Map<String, Object>> getPurchaseData(@PathVariable int companyId) {
        return companyService.getSaleData(companyId);
    }

    //EndPoint: localhost:8080/api/saveup/v1/companies/login
    //Method: POST
    @Transactional(readOnly = true)
    @PostMapping("/companies/login")
    public ResponseEntity<Company> getCompanyByEmailAndPassword(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Company company = companyRepository.findByEmailAndPassword(email, password);
        if (company != null) {
            return new ResponseEntity<>(company, HttpStatus.OK);
        } else {
            return null;
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/companies/recover
    //Method: POST
    @Transactional(readOnly = true)
    @PostMapping("/companies/recover")
    public ResponseEntity<Company> getCompanyByEmail(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");

        Company company = companyRepository.findByEmail(email);
        if (company != null) {
            return new ResponseEntity<>(company, HttpStatus.OK);
        } else {
            return null;
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/companies
    //Method: POST
    @Transactional
    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company company){
        validateCompany(company);
        existsCustomerByEmail(company);
        existsCompanyByEmailAndPhoneNumber(company);
        existsCompanyByEmail(company);
        existsCompanyByPhoneNumber(company);
        return new ResponseEntity<>(companyService.createCompany(company), HttpStatus.CREATED);
    }

    //EndPoint: localhost:8080/api/saveup/v1/companies/{id}
    //Method: PUT
    @Transactional
    @PutMapping("/companies/{id}")
    public ResponseEntity<Object> updateCompany(@PathVariable("id") int id,@RequestBody Company company){
        boolean isExist = companyService.isCompanyExist(id);
        if(isExist){
            validateCompany(company);
            company.setId(id);
            companyService.updateCompany(company);
            return new ResponseEntity<>("Company is updated succesfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al actualizar el company");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/companies/{id}
    //Method: DELETE
    @Transactional
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("id") int id){
        boolean isExist = companyService.isCompanyExist(id);
        if(isExist){
            companyService.deleteCompany(id);
            return new ResponseEntity<>("Company is deleted successfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al eliminar el company");
        }
    }

    private void validateCompany(Company company){
        String emailRegex = "^[A-Za-z0-9+_.-]+@(hotmail|gmail)\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(company.getEmail()).matches()) {
            throw new ValidationException("El email debe ser de tipo hotmail.com o gmail.com");
        }else if(company.getEmail() == null || company.getEmail().trim().isEmpty()){
            throw new ValidationException("El email debe ser obligatorio");
        }

        if(company.getName() == null || company.getName().trim().isEmpty()){
            throw new ValidationException("El nombre debe ser obligatorio");
        }

        if(company.getRuc() == null || company.getRuc().trim().isEmpty()){
            throw new ValidationException("El RUC debe ser obligatorio");
        } else if(company.getRuc().length() != 11){
            throw new ValidationException("El RUC debe tener 11 digitos");
        }

        if (company.getDepartment() == null || company.getDepartment().trim().isEmpty()){
            throw new ValidationException("El departamento debe ser obligatorio");
        }

        if (company.getDistrict()==null || company.getDistrict().trim().isEmpty()){
            throw new ValidationException("El distrito debe ser obligatorio");
        }

        String phoneNumberRegex = "^9\\d{8}$";
        pattern = Pattern.compile(phoneNumberRegex);
        if (!pattern.matcher(company.getPhoneNumber()).matches()) {
            throw new ValidationException("El numero telefonico debe empezar con 9 y tener 9 digitos");
        } else if (company.getPhoneNumber() == null || company.getPhoneNumber().trim().isEmpty()) {
            throw new ValidationException("El numero telefonico debe ser obligatorio");
        }

        if(company.getPassword() == null || company.getPassword().trim().isEmpty()){
            throw new ValidationException("La contraseña debe ser obligatorio");
        }

        if(company.getRepeatPassword() == null || company.getRepeatPassword().trim().isEmpty()){
            throw new ValidationException("La confirmación de contraseña debe ser obligatorio");
        }

        if (company.getPassword().length() < 8 || company.getPassword().length() > 12) {
            throw new ValidationException("La contraseña debe tener entre 8 y 12 caracteres");
        }

        if (!company.getPassword().equals(company.getRepeatPassword())){
            throw new ValidationException("La contraseña y la confirmación de contraseña no coinciden");
        }
    }

    private void existsCompanyByEmailAndPhoneNumber(Company company){
        if(companyRepository.existsByEmailAndPhoneNumber(company.getEmail(), company.getPhoneNumber())){
            throw new ValidationException("No se puede registrar la empresa porque existe uno con el email y numero telefonico");
        }
    }
    private void existsCompanyByEmail(Company company){
        if(companyRepository.existsByEmail(company.getEmail())){
            throw new ValidationException("No se puede registrar la empresa porque existe una cuenta con el email");
        }
    }
    private void existsCompanyByPhoneNumber(Company company){
        if(companyRepository.existsByPhoneNumber(company.getPhoneNumber())){
            throw new ValidationException("No se puede registrar la empresa porque existe uno con el numero telefonico");
        }
    }

    private void existsCustomerByEmail(Company company) {
        if(customerRepository.existsByEmail(company.getEmail())){
            throw new ValidationException("No se puede registrar la empresa porque existe una cuenta con el email");
        }
    }
}
