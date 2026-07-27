package com.monamatti.service;

import com.monamatti.entity.Reservation;
import com.monamatti.repository.ReservationRepository;
import com.monamatti.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testReservation = new Reservation("John Doe", "data:image/png;base64,12345");
        testReservation.setId(100L);
    }

    @Test
    @DisplayName("Should save and return reservation")
    void testSaveReservation() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation saved = reservationService.saveReservation(testReservation);

        assertNotNull(saved);
        assertEquals("John Doe", saved.getFullName());
        assertEquals("MM-000100", saved.getFormattedId());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    @DisplayName("Should return all reservations ordered chronologically descending")
    void testGetAllReservations() {
        when(reservationRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(testReservation));

        List<Reservation> list = reservationService.getAllReservations();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("John Doe", list.get(0).getFullName());
        verify(reservationRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Should delete reservation by ID")
    void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(100L);

        reservationService.deleteReservation(100L);

        verify(reservationRepository, times(1)).deleteById(100L);
    }
}
