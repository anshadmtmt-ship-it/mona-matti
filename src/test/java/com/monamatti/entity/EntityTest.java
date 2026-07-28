package com.monamatti.entity;

import com.monamatti.exception.GlobalExceptionHandler;
import com.monamatti.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    @DisplayName("Test Product getters, setters, equals, hashCode and toString")
    void testProductEntity() {

        Product p1 = new Product("Name", "Hero", "Sub", 49.99, "Desc", "img.svg", "IN_STOCK");
        p1.setId(1L);

        Product p2 = new Product();
        p2.setId(1L);
        p2.setName("Name");

        assertEquals(1L, p1.getId());
        assertEquals("Name", p1.getName());
        assertEquals("Hero", p1.getHeroTitle());
        assertEquals("Sub", p1.getHeroSubtitle());
        assertEquals(49.99, p1.getPrice());
        assertEquals("Desc", p1.getDescription());
        assertEquals("img.svg", p1.getMainImage());
        assertEquals("IN_STOCK", p1.getStockStatus());

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertTrue(p1.toString().contains("MONA MATTI") || p1.toString().contains("Product"));
    }

    @Test
    @DisplayName("Test Reservation getters, formattedId, equals, hashCode and toString")
    void testReservationEntity() {

        Reservation r1 = new Reservation("Jane Doe");
        r1.setId(1L);

        Reservation r2 = new Reservation();
        r2.setId(1L);

        assertEquals(1L, r1.getId());
        assertEquals("Jane Doe", r1.getFullName());
        assertEquals("MM-000001", r1.getFormattedId());

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertTrue(r1.toString().contains("Reservation"));
    }

    @Test
    @DisplayName("Test GlobalExceptionHandler exception handling")
    void testGlobalExceptionHandler() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Model model = new ConcurrentModel();

        String view1 = handler.handleResourceNotFound(
                new ResourceNotFoundException("Not found"),
                model);

        assertEquals("error", view1);
        assertEquals("Not found", model.getAttribute("errorMessage"));

        Model model2 = new ConcurrentModel();

        String view2 = handler.handleGeneralException(
                new RuntimeException("Server error"),
                model2);

        assertEquals("error", view2);
        assertNotNull(model2.getAttribute("errorMessage"));
    }
}