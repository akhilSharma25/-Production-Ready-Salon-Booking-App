package com.akhil.controller;

import com.akhil.DTO.BookingDTO;
import com.akhil.DTO.NotificationDto;
import com.akhil.mapper.NotificationsMapper;
import com.akhil.model.Notification;
import com.akhil.service.NotificationService;
import com.akhil.service.client.BookingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/salon-owner")
public class SalonNotificationController {
    private final NotificationService service;
    private final BookingClient bookingClient;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationDto>> getNotificationBySalonId(@PathVariable Long salonId){

        List<Notification> notifications=service.getAllNotificationBySalonId(salonId);

        List<NotificationDto> notificationDtos=notifications.stream().
                map(notification -> {
                    BookingDTO bookingDTO=bookingClient.getBookingById(notification.getBookingId()).getBody();
                    return NotificationsMapper.toDTO(notification,bookingDTO);
                }).collect(Collectors.toList());
        return ResponseEntity.ok(notificationDtos);
    }
}
