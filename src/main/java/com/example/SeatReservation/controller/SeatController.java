package com.example.SeatReservation.controller;

import com.example.SeatReservation.model.Seat;
import com.example.SeatReservation.repository.SeatRepository;
import com.example.SeatReservation.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @ResponseBody
    public String reserveSeat(@RequestParam String seatCode,
                              @RequestParam String name,
                              @RequestParam String password,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        seatService.reserveSeat(seatCode, name, password, startDate, endDate);
        return "reserved";
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
