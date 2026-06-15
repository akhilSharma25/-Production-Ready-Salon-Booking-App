package com.akhil.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingSlotDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
