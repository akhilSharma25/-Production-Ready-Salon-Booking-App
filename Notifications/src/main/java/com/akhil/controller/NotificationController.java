package com.akhil.controller;

import com.akhil.DTO.BookingDTO;
import com.akhil.DTO.NotificationDto;
import com.akhil.mapper.NotificationsMapper;
import com.akhil.model.Notification;
import com.akhil.service.NotificationService;
import com.akhil.service.client.BookingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(
            @RequestBody Notification notification
            ){

        return ResponseEntity.ok(service.createNotification(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotificationByUserId(@PathVariable Long userId){

        List<Notification> notifications=service.getAllNotificationByUserId(userId);

        List<NotificationDto> notificationDtos=notifications.stream().
                map(notification -> {
                    BookingDTO bookingDTO=bookingClient.getBookingById(notification.getBookingId()).getBody();
                   return NotificationsMapper.toDTO(notification,bookingDTO);
                }).collect(Collectors.toList());
        return ResponseEntity.ok(notificationDtos);
    }



    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDto> markNotificationAsRead(
            @PathVariable Long notificationId
    ){
        Notification notification=service.markNotificationsAsRead(notificationId);

        BookingDTO bookingDTO=bookingClient.getBookingById(notification.getBookingId()).getBody();

        return ResponseEntity.ok(NotificationsMapper.toDTO(notification,bookingDTO));
    }

}
