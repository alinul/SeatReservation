package com.example.SeatReservation.service;

import com.example.SeatReservation.model.Seat;
import com.example.SeatReservation.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public void reserveSeat(String seatCode, String name, String password, LocalDate startDate, LocalDate endDate) {
        seatRepository.findBySeatCode(seatCode).ifPresent(seat -> {
            seat.setReserved(true);
            seat.setReservedBy(name);
            seat.setPassword(password);
            seat.setStartDate(startDate);
            seat.setEndDate(endDate);
            seatRepository.save(seat);
        });
    }

    public boolean cancelReservation(String seatCode, String password) {
        Optional<Seat> optionalSeat = seatRepository.findBySeatCode(seatCode);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            if (seat.getPassword() != null && seat.getPassword().equals(password)) {
                seat.setReserved(false);
                seat.setReservedBy(null);
                seat.setPassword(null);
                seatRepository.save(seat);
                return true;
            }
        }
        return false;
    }
}