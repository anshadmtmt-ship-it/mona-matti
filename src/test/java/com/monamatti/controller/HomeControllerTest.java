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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

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
    @DisplayName("Should serve public index page with product model attribute")
    void testIndexPage() throws Exception {
        when(productService.getFeaturedProduct()).thenReturn(testProduct);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("product"));
    }
}
