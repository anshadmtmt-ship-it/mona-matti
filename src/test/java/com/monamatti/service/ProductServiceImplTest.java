package com.monamatti.service;

import com.monamatti.entity.Product;
import com.monamatti.exception.ResourceNotFoundException;
import com.monamatti.repository.ProductRepository;
import com.monamatti.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(
                "MONA MATTI Pencil",
                "One Pencil. Infinite Possibilities.",
                "Replace the nib.",
                49.99,
                "Description",
                "/image.svg",
                "IN_STOCK"
        );
        testProduct.setId(1L);
    }

    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MONA MATTI Pencil", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return product by ID when product exists")
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product ID does not exist")
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should return featured product when DB contains product")
    void testGetFeaturedProduct_Found() {
        when(productRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(testProduct));

        Product result = productService.getFeaturedProduct();

        assertNotNull(result);
        assertEquals("MONA MATTI Pencil", result.getName());
        verify(productRepository, times(1)).findFirstByOrderByIdAsc();
    }

    @Test
    @DisplayName("Should return fallback product when DB is empty")
    void testGetFeaturedProduct_Fallback() {
        when(productRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        Product result = productService.getFeaturedProduct();

        assertNotNull(result);
        assertEquals("MONA MATTI Pencil", result.getName());
        verify(productRepository, times(1)).findFirstByOrderByIdAsc();
    }

    @Test
    @DisplayName("Should save and return product")
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.saveProduct(testProduct);

        assertNotNull(result);
        assertEquals("MONA MATTI Pencil", result.getName());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Should delete product by ID")
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
