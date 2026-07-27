package com.monamatti.service;

import com.monamatti.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product getFeaturedProduct();
    Product saveProduct(Product product);
    void deleteProduct(Long id);
}
