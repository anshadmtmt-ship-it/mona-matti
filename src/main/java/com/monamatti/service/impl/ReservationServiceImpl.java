package com.monamatti.service.impl;

import com.monamatti.entity.Reservation;
import com.monamatti.repository.ReservationRepository;
import com.monamatti.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation saved successfully: id={}, name={}", saved.getId(), saved.getFullName());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
        log.info("Reservation deleted: id={}", id);
    }
}
