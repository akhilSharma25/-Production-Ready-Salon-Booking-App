package com.akhil.service.imp;

import com.akhil.DTO.BookingDTO;
import com.akhil.DTO.NotificationDto;
import com.akhil.mapper.NotificationsMapper;
import com.akhil.model.Notification;
import com.akhil.repo.NotificationRepo;
import com.akhil.service.NotificationService;
import com.akhil.service.client.BookingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepo repo;
    private final BookingClient bookingClient;
    @Override
    public NotificationDto createNotification(Notification notification) {
        Notification savedNotification=repo.save(notification);
        BookingDTO bookingDTO=bookingClient.getBookingById(savedNotification.getBookingId()).getBody();

        NotificationDto notificationDto= NotificationsMapper.toDTO(savedNotification,bookingDTO);

        return notificationDto;
    }

    @Override
    public List<Notification> getAllNotificationByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<Notification> getAllNotificationBySalonId(Long salonId) {
        return repo.findBySalonId(salonId);
    }

    @Override
    public Notification markNotificationsAsRead(Long notificationId) {

        return repo.findById(notificationId).map(notification ->
        {
            notification.setIsRead(true);
            return repo.save(notification);
        }).orElseThrow(()->new RuntimeException("Notification not found"));
    }
}
