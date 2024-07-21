package dev.limjustin.gsreservation.controller;

import dev.limjustin.gsreservation.domain.Reservation;
import dev.limjustin.gsreservation.domain.TimeSlot;
import dev.limjustin.gsreservation.dto.ReservationRequest;
import dev.limjustin.gsreservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Controller
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/heroes")
    public String homePage() {
        return "home";
    }

    // 메인 화면 API
    @GetMapping("/heroes/schedule")
    public String showMainPage(Model model) {
        List<List<TimeSlot>> slots = new ArrayList<>();
        slots.add(reservationService.getTimeSlotsByDate("2024-07-22"));
        slots.add(reservationService.getTimeSlotsByDate("2024-07-23"));
        slots.add(reservationService.getTimeSlotsByDate("2024-07-24"));
        model.addAttribute("slots", slots);
        return "schedule";
    }

    // 예약하기 화면 API
    @GetMapping("/heroes/reservation")
    public String showReservationForm(@RequestParam("date") String date, @RequestParam("time") String time, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        return "reservation";
    }

    // 예약하기 API
    @PostMapping("/api/v1/reservation")
    public String makeReservation(@RequestParam("username") String username, @RequestParam("date") String date, @RequestParam("time") String time, Model model) throws InterruptedException, ExecutionException {
        CompletableFuture<String> stringCompletableFuture = reservationService.makeReservation(ReservationRequest.SaveDto.builder()
                .username(username)
                .date(date)
                .time(time)
                .build());
        System.out.println("stringCompletableFuture = " + stringCompletableFuture);
        System.out.println("stringCompletableFuture.get() = " + stringCompletableFuture.get());

        String status = stringCompletableFuture.get().split("_")[0];
        String message = stringCompletableFuture.get().split("_")[1];

        if (status.equals("failed")) {
            model.addAttribute("message", message);
            return "failed";
        } else {
            model.addAttribute("message", message);
            return "success";
        }
    }

    // 예약 조회하기 API
    @GetMapping("/heroes/details")
    public String showDetails() {
        return "details";
    }

    @GetMapping("/api/v1/details")
    @ResponseBody
    public Reservation showReservation(@RequestParam("username") String username) {
        return reservationService.showReservation(username);
    }

    @GetMapping("/healthcheck")
    @ResponseBody
    public String showHealthcheck() {
        return "Hello world!";
    }
}
