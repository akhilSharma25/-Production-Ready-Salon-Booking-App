package com.akhil.mapper;

import com.akhil.DTO.BookingDTO;
import com.akhil.DTO.NotificationDto;
import com.akhil.model.Notification;
import lombok.Data;

public class NotificationsMapper {

    public  static NotificationDto toDTO(Notification notification, BookingDTO bookingDTO){
        NotificationDto notificationDto=new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setType(notification.getType());
        notificationDto.setIsRead(notification.getIsRead());
        notificationDto.setDescription(notification.getDescription());
        notificationDto.setBookingId(bookingDTO.getId());
        notificationDto.setUserId(notification.getUserId());
        notificationDto.setSalonId(notification.getSalonId());
        notificationDto.setCreatedAt(notification.getCreatedAt());
    }
}
