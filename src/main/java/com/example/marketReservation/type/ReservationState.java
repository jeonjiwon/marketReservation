package com.example.marketReservation.type;

public enum ReservationState {
    REQUESTED, // 예약요청(0)
    ARRIVED,   // 도착(1)
    COMPLETED, // 완료(2)
    CANCELED,   // 취소(3)
    REJECT,     // 거절(4)
    APPROVAL    // 승인(5)
}
