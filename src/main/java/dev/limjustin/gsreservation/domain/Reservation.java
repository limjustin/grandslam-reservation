package dev.limjustin.gsreservation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예약 정보
    private String date;
    private String time;

    // 신원 확인 정보
    private String username;

    @Builder
    public Reservation(String date, String time, String username) {
        this.date = date;
        this.time = time;
        this.username = username;
    }
}
