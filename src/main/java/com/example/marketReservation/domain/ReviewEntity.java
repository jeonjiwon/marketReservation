package com.example.marketReservation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "REVIEW")
@EntityListeners(AuditingEntityListener.class)
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //리뷰등록번호

    private LocalDateTime registerDt; //등록일시

    private Long registerUserId; //등록아이디

    private Long storeId; //리뷰 작성하는 상점

    private String content; //내용

    private Long rating; //별점

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
