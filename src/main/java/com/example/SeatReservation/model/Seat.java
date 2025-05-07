package com.example.SeatReservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String seatCode;

    private boolean reserved = false;

    @Column(name = "reserved_by")
    private String reservedBy;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(name = "password")
    private String password;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Seat(String seatCode, int x, int y, LocalDate startDate, LocalDate endDate) {
        this.seatCode = seatCode;
        this.x = x;
        this.y = y;
        this.reserved = false;
        this.reservedBy = null;
        this.password = null;
        this.startDate=startDate;
        this.endDate=endDate;
    }
}
