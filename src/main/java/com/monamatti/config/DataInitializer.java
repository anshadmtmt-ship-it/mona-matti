package com.monamatti.config;

import com.monamatti.entity.Product;
import com.monamatti.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String DEFAULT_PROD_NAME = "MONA MATTI Multi-Nib Pencil";
    private static final String DEFAULT_HERO_TITLE = "One Pencil. Infinite Possibilities.";
    private static final String DEFAULT_HERO_SUBTITLE = "Replace the nib, Refresh the idea. Create without limits.";
    private static final double DEFAULT_PRICE = 49.99;
    private static final String DEFAULT_DESC = "Anodized matte aluminum body with 5 interlocking 0.7mm HB artist-grade graphite nibs and a heavy-duty brass push mechanism.";
    private static final String DEFAULT_IMAGE = "/images/pencil-hero.svg";
    private static final String DEFAULT_STOCK = "IN_STOCK";

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking database tables for default seed data...");

        if (productRepository.count() == 0) {
            log.info("Seeding default Product details...");
            Product product = new Product(
                    DEFAULT_PROD_NAME, DEFAULT_HERO_TITLE, DEFAULT_HERO_SUBTITLE,
                    DEFAULT_PRICE, DEFAULT_DESC, DEFAULT_IMAGE, DEFAULT_STOCK
            );
            productRepository.save(product);
        }

        log.info("Database initialization check complete. Database ready.");
    }
}
