package com.example.SeatReservation.controller;

import com.example.SeatReservation.model.Seat;
import com.example.SeatReservation.repository.SeatRepository;
import com.example.SeatReservation.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatService seatService;



    @GetMapping("/")
    public String showSeats(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            Model model) {
        List<Seat> allSeats = seatRepository.findAll();

        if (date != null) {
            allSeats = allSeats.stream()
                    .filter(seat -> !(seat.isReserved() && (date.isBefore(seat.getStartDate()) || date.isAfter(seat.getEndDate()))))
                    .toList();
        }

        model.addAttribute("seats", allSeats);
        model.addAttribute("selectedDate", date);
        return "map";
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSeat(@RequestParam String seatCode,
                                              @RequestParam String name,
                                              @RequestParam String password,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Optional<Seat> optionalSeat = seatRepository.findBySeatCode(seatCode);

        if (optionalSeat.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seat not found.");
        }

        Seat seat = optionalSeat.get();

        if (seat.isReserved()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Seat already reserved!");
        }

        seat.setReserved(true);
        seat.setReservedBy(name);
        seat.setPassword(password);
        seat.setStartDate(startDate);
        seat.setEndDate(endDate);
        seatRepository.save(seat);

        return ResponseEntity.ok("Seat reserved successfully.");
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelSeat(@RequestParam String seatCode, @RequestParam String password) {
        boolean success = seatService.cancelReservation(seatCode, password);
        if (success) {
            return ResponseEntity.ok("canceled");
        } else {
            return ResponseEntity.status(403).body("Incorrect password");
        }
    }


}
