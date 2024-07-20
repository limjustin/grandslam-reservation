package dev.limjustin.gsreservation.repository;

import dev.limjustin.gsreservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUsername(String username);
}
