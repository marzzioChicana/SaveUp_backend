package com.upc.saveup.service.impl;

import com.upc.saveup.dto.CartDto;
import com.upc.saveup.dto.ProductDto;
import com.upc.saveup.exception.ResourceNotFoundException;
import com.upc.saveup.model.Cart;
import com.upc.saveup.model.Company;
import com.upc.saveup.model.Product;
import com.upc.saveup.repository.CompanyRepository;
import com.upc.saveup.repository.ProductRepository;
import com.upc.saveup.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CompanyRepository companyRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = DtoToEntity(productDto);
        Company company = companyRepository.findById(productDto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la compañía con id: " + productDto.getCompanyId()));

        product.setCompany(company);
        return EntityToDto(productRepository.save(product));
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Product product = DtoToEntity(productDto);
        Company company = companyRepository.findById(productDto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la compañía con id: " + productDto.getCompanyId()));
        product.setCompany(company);
        productRepository.save(product);
    }

    @Override
    public Product getProduct(int id) { return productRepository.findById(id).get(); }

    @Override
    public void deleteProduct(int id) { productRepository.deleteById(id); }

    @Override
    public List<Product> getAllProducts() { return (List<Product>) productRepository.findAll(); }

    @Override
    public List<Product> getProductsByCompany(int companyId) { return productRepository.findByCompanyId(companyId); }

    @Override
    public boolean isProductExist(int id) { return productRepository.existsById(id); }

    private ProductDto EntityToDto(Product product) { return modelMapper.map(product, ProductDto.class); }

    private Product DtoToEntity(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }
}
