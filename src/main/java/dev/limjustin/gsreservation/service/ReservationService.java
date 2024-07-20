package dev.limjustin.gsreservation.service;

import dev.limjustin.gsreservation.domain.Reservation;
import dev.limjustin.gsreservation.domain.TimeSlot;
import dev.limjustin.gsreservation.dto.ReservationRequest;
import dev.limjustin.gsreservation.handler.ConcurrentRequestHandler;
import dev.limjustin.gsreservation.repository.ReservationRepository;
import dev.limjustin.gsreservation.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ConcurrentRequestHandler concurrentRequestHandler;

    public List<TimeSlot> getTimeSlotsByDate(String date) {
        return timeSlotRepository.findByDate(date);
    }

    @Async
    @Transactional
    public CompletableFuture<String> makeReservation(ReservationRequest.SaveDto saveDto) throws InterruptedException {
        System.out.println("saveDto = " + saveDto.getUsername());
        return concurrentRequestHandler.handleConcurrency(saveDto);
    }

    public Reservation showReservation(String username) {
        return reservationRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }
}
