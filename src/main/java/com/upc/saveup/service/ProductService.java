package com.upc.saveup.service;

import com.upc.saveup.dto.ProductDto;
import com.upc.saveup.model.Product;

import java.util.List;

public interface ProductService {
    public abstract ProductDto createProduct(ProductDto productDto);
    public abstract void updateProduct(ProductDto productDto);
    public abstract void deleteProduct(int id);
    public abstract Product getProduct(int id);
    public abstract List<Product> getAllProducts();
    public abstract List<Product> getProductsByCompany(int companyId);
    public abstract boolean isProductExist(int id);
}
