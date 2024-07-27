package com.example.marketReservation.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private LocalDateTime registerDt;
    private Long registerUserId;
    private Long storeId;
    private String content;
    private Long rating;
}
