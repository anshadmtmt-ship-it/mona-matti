package com.monamatti.controller;

import com.monamatti.entity.Reservation;
import com.monamatti.service.ReservationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private static final String REDIRECT_ADMIN_RESERVATIONS = "redirect:/admin/reservations";
    private static final String VIEW_ADMIN_RESERVATIONS = "admin/reservations";

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Public JSON endpoint — called via fetch() from the reservation modal.
     * Returns JSON with the formatted reservation ID and customer name on success.
     */
    @PostMapping("/reserve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitReservation(
            @Valid @ModelAttribute Reservation reservation,
            BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            log.warn("Reservation validation failed: {}", bindingResult.getAllErrors());
            response.put("success", false);
            response.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        Reservation saved = reservationService.saveReservation(reservation);
        log.info("New reservation accepted: id={}, name={}", saved.getId(), saved.getFullName());

        response.put("success", true);
        response.put("reservationId", saved.getFormattedId());
        response.put("fullName", saved.getFullName());
        return ResponseEntity.ok(response);
    }

    /**
     * Admin view: list all reservations.
     */
    @GetMapping("/admin/reservations")
    public String listReservations(Model model) {
        log.debug("Serving admin reservations list");
        model.addAttribute("reservations", reservationService.getAllReservations());
        return VIEW_ADMIN_RESERVATIONS;
    }

    /**
     * Admin action: delete a reservation.
     */
    @PostMapping("/admin/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.deleteReservation(id);
        log.info("Admin deleted reservation id={}", id);
        redirectAttributes.addFlashAttribute("successMessage", "Reservation deleted successfully.");
        return REDIRECT_ADMIN_RESERVATIONS;
    }
}
