package dev.limjustin.gsreservation.service;

import dev.limjustin.gsreservation.domain.Reservation;
import dev.limjustin.gsreservation.domain.TimeSlot;
import dev.limjustin.gsreservation.dto.ReservationRequest;
import dev.limjustin.gsreservation.handler.ConcurrentRequestHandler;
import dev.limjustin.gsreservation.repository.ReservationRepository;
import dev.limjustin.gsreservation.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    // 여기에 Async 어노테이션 달아서 생긴 문제..
    @Transactional
    public CompletableFuture<String> makeReservation(ReservationRequest.SaveDto saveDto) throws InterruptedException {
        System.out.println("saveDto = " + saveDto);
        return concurrentRequestHandler.handleConcurrency(saveDto);
    }

    public Reservation showReservation(String username) {
        return reservationRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }
}
