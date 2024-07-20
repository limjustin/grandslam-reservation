package dev.limjustin.gsreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReservationRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDto {
        private String username;
        private String date;
        private String time;
    }
}
