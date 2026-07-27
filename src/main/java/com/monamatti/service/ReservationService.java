package com.monamatti.service;

import com.monamatti.entity.Reservation;

import java.util.List;

public interface ReservationService {

    /** Save a new reservation and return the persisted entity with its generated ID. */
    Reservation saveReservation(Reservation reservation);

    /** Return all reservations ordered by creation date descending. */
    List<Reservation> getAllReservations();

    /** Delete a reservation by its ID. */
    void deleteReservation(Long id);
}
