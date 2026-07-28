package com.monamatti.controller;

import com.monamatti.entity.Reservation;
import com.monamatti.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testReservation = new Reservation("Jane Architect");
        testReservation.setId(1L);
    }

    @Test
    @DisplayName("Should successfully submit reservation via POST /reserve")
    void testSubmitReservation_Success() throws Exception {

        when(reservationService.saveReservation(any(Reservation.class)))
                .thenReturn(testReservation);

        mockMvc.perform(post("/reserve")
                        .param("fullName", "Jane Architect")
                        .param("signature", "data:image/png;base64,sig123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.reservationId").value("MM-000001"))
                .andExpect(jsonPath("$.fullName").value("Jane Architect"));
    }

    @Test
    @DisplayName("Should return HTTP 400 when validation fails")
    void testSubmitReservation_ValidationError() throws Exception {

        mockMvc.perform(post("/reserve")
                        .param("fullName", "")
                        .param("signature", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should serve admin reservations page")
    void testListReservations() throws Exception {

        when(reservationService.getAllReservations())
                .thenReturn(List.of(testReservation));

        mockMvc.perform(get("/admin/reservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservations"))
                .andExpect(model().attributeExists("reservations"));
    }

    @Test
    @DisplayName("Should delete reservation")
    void testDeleteReservation() throws Exception {

        doNothing().when(reservationService).deleteReservation(1L);

        mockMvc.perform(post("/admin/reservations/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reservations"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(reservationService, times(1)).deleteReservation(1L);
    }
}