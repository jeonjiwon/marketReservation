package com.example.marketReservation.model;

import com.example.marketReservation.domain.MemberEntity;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Market {
    private Long id;

    private String name;

    private String location;

    private String description;

    private String deleteYn;

    private Long adminId;
}
