package com.akhil.repo;

import com.akhil.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification,Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification>  findBySalonId(Long salonId);
}
