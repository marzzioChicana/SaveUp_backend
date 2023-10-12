package com.upc.saveup.controller;

import com.upc.saveup.dto.ProductDto;
import com.upc.saveup.exception.ResourceNotFoundException;
import com.upc.saveup.exception.ValidationException;
import com.upc.saveup.model.Company;
import com.upc.saveup.model.Product;
import com.upc.saveup.repository.CompanyRepository;
import com.upc.saveup.repository.ProductRepository;
import com.upc.saveup.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/api/saveup/v1")
public class ProductController {
    @Autowired
    private ProductService productService;

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public ProductController(ProductRepository productRepository, CompanyRepository companyRepository){
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
    }

    //EndPoint: localhost:8080/api/saveup/v1/products
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<List<Product>>(productRepository.findAll(), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/products/company/{id}
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/products/company/{id}")
    public ResponseEntity<List<Product>> getProductById(@PathVariable("id") int companyId){
        return new ResponseEntity<List<Product>>(productService.getProductsByCompany(companyId), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/products/{id}
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id){
        return new ResponseEntity<Product>(productService.getProduct(id), HttpStatus.OK);
    }

    //EndPoint: localhost:8080/api/saveup/v1/products
    //Method: POST
    @Transactional
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    //EndPoint: localhost:8080/api/saveup/v1/products/{id}
    //Method: PUT
    @Transactional
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") int id, @RequestBody ProductDto productDto){
        boolean isExist=productService.isProductExist(id);
        if(isExist){
            validateProduct(productDto);
            productDto.setId(id);
            productService.updateProduct(productDto);
            return new ResponseEntity<>("Product is updated succesfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al actualizar el product");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/products/{id}/stock
    //Method: PUT
    @Transactional
    @PutMapping("/products/{id}/stock")
    public ResponseEntity<Object> updateProductStock(@PathVariable("id") int id, @RequestBody int newStock) {
        boolean isExist=productService.isProductExist(id);
        if(isExist){
            Product productTemp = productRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("No se encontro el producto con id: " + id));

            ProductDto productDtoTemp = new ProductDto();
            productDtoTemp.setId(id);
            productDtoTemp.setName(productTemp.getName());
            productDtoTemp.setDescription(productTemp.getDescription());
            productDtoTemp.setPrice(productTemp.getPrice());
            productDtoTemp.setStock(newStock);
            productDtoTemp.setExpirationDate(productTemp.getExpirationDate());
            productDtoTemp.setImage(productTemp.getImage());
            productDtoTemp.setCompanyId(productTemp.getCompany().getId());

            productService.updateProduct(productDtoTemp);
            return new ResponseEntity<>("Product stock is updated succesfully", HttpStatus.OK);
        } else{
            throw new ValidationException("Error al actualizar el stock del product");
        }
    }

    //EndPoint: localhost:8080/api/saveup/v1/products/{id}
    //Method: DELETE
    @Transactional
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id){
        boolean isExist=productService.isProductExist(id);
        if(isExist){
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product is deleted succesfully", HttpStatus.OK);
        }else{
            throw new ValidationException("Error al eliminar el product");
        }
    }

    private void validateProduct(ProductDto productDto) {
        if(productDto.getName() == null || productDto.getName().isEmpty()){
            throw new ValidationException("El nombre del product no debe estar vacio");
        }

        if(productDto.getPrice() == null || productDto.getPrice().isNaN()){
            throw new ValidationException("El precio del product no debe estar vacio");
        } else if(productDto.getPrice() <= 0){
            throw new ValidationException("El precio del product debe ser mayor a 0");
        }

        if(productDto.getStock() <= 0){
            throw new ValidationException("El stock del product debe ser mayor a 0");
        }

        String expirationDateStr = productDto.getExpirationDate();
        if (expirationDateStr == null || expirationDateStr.isEmpty()) {
            throw new ValidationException("La fecha de vencimiento del producto no debe estar vacía");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false); // Desactivar el modo permisivo

        try {
            dateFormat.parse(expirationDateStr);
        } catch (ParseException e) {
            throw new ValidationException("La fecha de vencimiento debe tener el formato dd-MM-yyyy");
        }
    }

    private void existsProductByNameAndCompany(Product product, Company company) {
        if(productRepository.existsByNameAndCompany(product.getName(), company)){
            throw new ValidationException("Ya existe un product con el nombre y de la misma empresa");
        }
    }
}
