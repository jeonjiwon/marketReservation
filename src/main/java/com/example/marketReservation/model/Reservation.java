package com.example.marketReservation.model;

import com.example.marketReservation.type.ReservationState;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    private Long id;
    private LocalDateTime reservationDt;
    private Long storeId;
    private Long userId;
    private String phoneNumber;
    private ReservationState reservationState;
    private String rmkDc;
}
