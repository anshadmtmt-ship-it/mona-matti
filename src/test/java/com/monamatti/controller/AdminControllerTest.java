package com.monamatti.controller;

import com.monamatti.entity.Product;
import com.monamatti.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

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
    }

    @Test
    @DisplayName("Should serve Admin Dashboard page")
    void testDashboardPage() throws Exception {
        when(productService.getFeaturedProduct()).thenReturn(testProduct);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @DisplayName("Should save product details and redirect to /admin")
    void testSaveProduct_Success() throws Exception {
        when(productService.saveProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/admin/product/save")
                        .param("name", "Updated Pencil")
                        .param("heroTitle", "Updated Hero Title")
                        .param("price", "59.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("Should redirect with error when product validation fails")
    void testSaveProduct_ValidationError() throws Exception {
        mockMvc.perform(post("/admin/product/save")
                        .param("name", "")
                        .param("heroTitle", "")
                        .param("price", "-10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
