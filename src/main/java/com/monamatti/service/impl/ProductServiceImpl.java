package com.monamatti.service.impl;

import com.monamatti.entity.Product;
import com.monamatti.exception.ResourceNotFoundException;
import com.monamatti.repository.ProductRepository;
import com.monamatti.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String DEFAULT_NAME = "MONA MATTI Pencil";
    private static final String DEFAULT_HERO_TITLE = "One Pencil. Infinite Possibilities.";
    private static final String DEFAULT_HERO_SUBTITLE = "Replace the nib, Refresh the idea. Create without limits.";
    private static final double DEFAULT_PRICE = 49.99;
    private static final String DEFAULT_DESC = "Premium Multi-Nib Pencil";
    private static final String DEFAULT_IMAGE = "/images/hero-pencil.png";
    private static final String DEFAULT_STOCK = "IN_STOCK";

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getFeaturedProduct() {
        return productRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> new Product(DEFAULT_NAME, DEFAULT_HERO_TITLE, DEFAULT_HERO_SUBTITLE,
                        DEFAULT_PRICE, DEFAULT_DESC, DEFAULT_IMAGE, DEFAULT_STOCK));
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
