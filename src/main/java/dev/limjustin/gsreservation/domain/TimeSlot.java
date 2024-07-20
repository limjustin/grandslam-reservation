package dev.limjustin.gsreservation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class TimeSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String time;
    private boolean booked;

    public TimeSlot(String date, String time, boolean booked) {
        this.date = date;
        this.time = time;
        this.booked = booked;
    }

    public void setBooked() {
        this.booked = true;
    }
}
