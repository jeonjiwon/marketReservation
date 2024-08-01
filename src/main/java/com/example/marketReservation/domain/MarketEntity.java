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
@Entity(name = "MARKET")
@EntityListeners(AuditingEntityListener.class)
public class MarketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상점아이디

    @Column(nullable = false)
    private String name; //상점명

    private String location; //위치

    private String description; //설명

    private Double rating; //상점 평균 별점

    private Long ratingCount; // 상점 리뷰 작성 수

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "adminId", nullable = false)
//    private MemberEntity adminId;

    private Long adminId; //매장관리자회원ID

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addAverRating(Double rating, Long ratingCount) {
        this.ratingCount = ratingCount + 1;
        this.rating = (this.rating * ratingCount + rating) / (double)this.ratingCount;
    }

    public void deleteAverRating(Double rating, Long ratingCount) {
        this.ratingCount = ratingCount - 1;
        this.rating = (Double)(this.rating * ratingCount - rating) / this.ratingCount;
    }
}
