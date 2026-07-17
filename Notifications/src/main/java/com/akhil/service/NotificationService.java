package com.akhil.service;

import com.akhil.DTO.NotificationDto;
import com.akhil.model.Notification;

import java.util.List;

public interface NotificationService {

    NotificationDto createNotification(Notification notification);
    List<Notification>  getAllNotificationByUserId(Long userId);
    List<Notification>  getAllNotificationBySalonId(Long salonId);
    Notification  markNotificationsAsRead(Long notificationId);
}
