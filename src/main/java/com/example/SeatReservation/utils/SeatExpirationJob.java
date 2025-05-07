package com.example.SeatReservation.utils;

import com.example.SeatReservation.model.Seat;
import com.example.SeatReservation.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SeatExpirationJob {

    @Autowired
    private SeatRepository seatRepository;

    @Scheduled(fixedRate = 600000)
    public void clearExpiredReservations() {
        LocalDate today = LocalDate.now();
        List<Seat> expiredSeats = seatRepository.findAll().stream()
                .filter(seat -> seat.isReserved()
                        && seat.getEndDate() != null
                        && seat.getEndDate().isBefore(today))
                .toList();

        expiredSeats.forEach(seat -> {
            seat.setReserved(false);
            seat.setReservedBy(null);
            seat.setPassword(null);
            seat.setStartDate(null);
            seat.setEndDate(null);
        });

        seatRepository.saveAll(expiredSeats);
        System.out.println("Expired reservations cleared: " + expiredSeats.size());
    }
}