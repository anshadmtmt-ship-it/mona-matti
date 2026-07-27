package com.monamatti.config;

import com.monamatti.entity.Product;
import com.monamatti.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    @DisplayName("Should seed default product when DB count is 0")
    void testRun_SeedData() {
        when(productRepository.count()).thenReturn(0L);

        dataInitializer.run();

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should skip seeding when product already exists")
    void testRun_DataAlreadyExists() {
        when(productRepository.count()).thenReturn(1L);

        dataInitializer.run();

        verify(productRepository, never()).save(any(Product.class));
    }
}
