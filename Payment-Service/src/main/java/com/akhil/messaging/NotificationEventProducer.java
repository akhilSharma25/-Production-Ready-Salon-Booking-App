package com.akhil.messaging;

import com.akhil.model.PaymentOrder;
import com.akhil.payload.DTO.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void notificationEvent(Long userId,Long bookingId,Long salonId){
        NotificationDto notificationDto=new NotificationDto();
        notificationDto.setBookingId(bookingId);
        notificationDto.setUserId(userId);
        notificationDto.setSalonId(salonId);
        notificationDto.setDescription("New Booking got confirmed");
        notificationDto.setType("BOOKING");
        rabbitTemplate.convertAndSend("notification-queue",notificationDto);

    }
}
