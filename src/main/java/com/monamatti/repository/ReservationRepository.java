package com.monamatti.repository;

import com.monamatti.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /** Return all reservations ordered most recent first for admin display. */
    List<Reservation> findAllByOrderByCreatedAtDesc();
}
