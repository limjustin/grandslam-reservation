package dev.limjustin.gsreservation.repository;

import dev.limjustin.gsreservation.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDate(String date);
    TimeSlot findByDateAndTime(String date, String time);
}
