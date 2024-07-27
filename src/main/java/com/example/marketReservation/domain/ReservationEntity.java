package com.example.marketReservation.domain;

import com.example.marketReservation.type.ReservationState;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "RESERVATION")
@EntityListeners(AuditingEntityListener.class)
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //예약번호

    @Column(nullable = false)
    private LocalDateTime reservationDt; //예약시간

    @Column(nullable = false)
    private Long storeId; //예약매장

    @Column(nullable = false)
    private Long userId; //예약자

    @Column(nullable = false)
    private String phoneNumber; //예약자번호

    @Column(nullable = false)
    private ReservationState reservationState; //예약상태

    private String rmkDc; //비고

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
