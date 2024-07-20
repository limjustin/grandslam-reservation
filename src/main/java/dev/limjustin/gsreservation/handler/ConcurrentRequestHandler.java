package dev.limjustin.gsreservation.handler;

import dev.limjustin.gsreservation.domain.Reservation;
import dev.limjustin.gsreservation.domain.TimeSlot;
import dev.limjustin.gsreservation.dto.ReservationRequest;
import dev.limjustin.gsreservation.repository.ReservationRepository;
import dev.limjustin.gsreservation.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class ConcurrentRequestHandler {

    private final RedissonClient redissonClient;
    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Async
    @Transactional
    public CompletableFuture<String> handleConcurrency(ReservationRequest.SaveDto requestDto) throws InterruptedException {
        try {
            Reservation reservation = handleRequest(requestDto);
            String responseString = reservation.getUsername() + "님, 예약이 완료되었습니다.";
            return CompletableFuture.completedFuture(responseString);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
    }

    private Reservation handleRequest(ReservationRequest.SaveDto requestDto) throws InterruptedException {
        // Lock 이름 생성 및 RLock 생성
        String lockName = getLockName(requestDto);
        RLock rlock = redissonClient.getLock(lockName);

        Reservation savedReservation = null;

        try {
            if (rlock.tryLock(10, TimeUnit.SECONDS)) {  // 1. 락을 선점합니다.
                // 2. 예약 내역을 조회합니다.
                TimeSlot timeSlot = timeSlotRepository.findByDateAndTime(requestDto.getDate(), requestDto.getTime());

                // 2-1. 예약 내역이 존재하면, 시간대를 파악합니다.
                if (timeSlot.isBooked()) {  // 예약 내역이 이미 존재
                    System.out.println("Reservation Duplicated");
                    throw new InterruptedException();
                }

                // 3. 예약 가능한 상태라면, 예약을 진행합니다. DB에 저장합니다.
                timeSlot.setBooked();
                savedReservation = reservationRepository.save(Reservation.builder()
                        .date(requestDto.getDate())
                        .time(requestDto.getTime())
                        .username(requestDto.getUsername())
                        .build());

                // 4. 락을 해제합니다.
                rlock.unlock();

            } else {
                System.out.println("Cannot acquire lock");
            }
        } catch (InterruptedException | RuntimeException e) {
            // e.printStackTrace();
            throw new InterruptedException();
        }

        return savedReservation;
    }

    private String getLockName(ReservationRequest.SaveDto requestDto) {
        return requestDto.getDate() + "_" + requestDto.getTime();
    }
}
